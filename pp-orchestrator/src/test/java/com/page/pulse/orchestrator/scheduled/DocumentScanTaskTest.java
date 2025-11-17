package com.page.pulse.orchestrator.scheduled;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.page.pulse.orchestrator.pojo.Document;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;
import com.page.pulse.orchestrator.rule.engine.DocumentRuleEngine;
import com.page.pulse.orchestrator.service.ConfluenceApiService;

/**
 * Unit tests for {@link DocumentScanTask}.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class DocumentScanTaskTest
{

    @Mock
    private ConfluenceApiService apiService;
    @Mock
    private DocumentRuleEngine ruleEngine;
    @InjectMocks
    private DocumentScanTask documentScanTask;
    private Document sampleDocument;

    @BeforeEach
    void setUp()
    {
        sampleDocument = new Document( "doc1", "owner1", "Title", "current", List.of( "tag1" ), LocalDateTime.now(),
            LocalDateTime.now() );
    }

    @Test
    void testDocumentScanTaskWhenRulePasses()
    {
        // given
        RuleResult passResult = RuleResult.pass( "doc1" );
        RuleEvaluation evaluation = new RuleEvaluation( "RuleA", passResult );
        when( apiService.collectPages( any() ) ).thenReturn( List.of( sampleDocument ) );
        when( ruleEngine.evaluate( sampleDocument ) ).thenReturn( List.of( evaluation ) );

        // when
        documentScanTask.documentScanTask();

        // then
        verify( apiService, times( 1 ) ).collectPages( any() );
        verify( ruleEngine, times( 1 ) ).evaluate( sampleDocument );
    }

    @Test
    void testDocumentScanTaskWhenRuleFails()
    {
        // given
        RuleResult failResult = RuleResult.fail( "Some error", "doc1" );
        RuleEvaluation evaluation = new RuleEvaluation( "RuleA", failResult );
        when( apiService.collectPages( any() ) ).thenReturn( List.of( sampleDocument ) );
        when( ruleEngine.evaluate( sampleDocument ) ).thenReturn( List.of( evaluation ) );

        // when
        documentScanTask.documentScanTask();

        // then
        verify( apiService, times( 1 ) ).collectPages( any() );
        verify( ruleEngine, times( 1 ) ).evaluate( sampleDocument );
    }

    @Test
    void testDocumentScanTaskWhenNoDocuments()
    {
        // given
        when( apiService.collectPages( any() ) ).thenReturn( List.of() );

        // when
        documentScanTask.documentScanTask();

        // then
        verify( apiService, times( 1 ) ).collectPages( any() );
        verify( ruleEngine, never() ).evaluate( any() );
    }
}
