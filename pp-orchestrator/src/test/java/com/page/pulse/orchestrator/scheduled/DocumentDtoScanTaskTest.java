package com.page.pulse.orchestrator.scheduled;

import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;
import com.page.pulse.orchestrator.rule.engine.DocumentRuleEngine;
import com.page.pulse.orchestrator.service.ConfluenceApiService;
import com.page.pulse.orchestrator.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DocumentScanTask}.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class DocumentDtoScanTaskTest
{

    @Mock
    private ConfluenceApiService apiService;
    @Mock
    private DocumentRuleEngine ruleEngine;
    @Mock
    private DocumentService documentService;
    @InjectMocks
    private DocumentScanTask documentScanTask;
    private DocumentDto sampleDocumentDto;

    @BeforeEach
    void setUp()
    {
        sampleDocumentDto = new DocumentDto( "doc1", "owner1", "Title", "current", List.of( "tag1" ), LocalDateTime.now(),
            LocalDateTime.now() );
    }

    @Test
    void testDocumentScanTaskWhenRulePasses()
    {
        // given
        final RuleResult passResult = RuleResult.pass( "doc1" );
        final RuleEvaluation evaluation = new RuleEvaluation( "RuleA", passResult );

        // when
        when( apiService.collectPages( any() ) ).thenReturn( List.of( sampleDocumentDto ) );
        when( ruleEngine.evaluate( sampleDocumentDto ) ).thenReturn( List.of( evaluation ) );

        documentScanTask.documentScanTask();

        // then
        verify( documentService ).saveOrUpdate( sampleDocumentDto );
        verify( apiService, times( 1 ) ).collectPages( any() );
        verify( ruleEngine, times( 1 ) ).evaluate( sampleDocumentDto );
    }

    @Test
    void testDocumentScanTaskWhenRuleFails()
    {
        // given
        final RuleResult failResult = RuleResult.fail( "Some error", "doc1" );
        final RuleEvaluation evaluation = new RuleEvaluation( "RuleA", failResult );

        // when
        when( apiService.collectPages( any() ) ).thenReturn( List.of( sampleDocumentDto ) );
        when( ruleEngine.evaluate( sampleDocumentDto ) ).thenReturn( List.of( evaluation ) );

        documentScanTask.documentScanTask();

        // then
        verify( documentService ).saveOrUpdate( sampleDocumentDto );
        verify( apiService, times( 1 ) ).collectPages( any() );
        verify( ruleEngine, times( 1 ) ).evaluate( sampleDocumentDto );
    }

    @Test
    void testDocumentScanTaskWhenNoDocuments()
    {
        // given, when
        when( apiService.collectPages( any() ) ).thenReturn( List.of() );

        documentScanTask.documentScanTask();

        // then
        verify( documentService, never() ).saveOrUpdate( any() );
        verify( apiService, times( 1 ) ).collectPages( any() );
        verify( ruleEngine, never() ).evaluate( any() );
    }
}
