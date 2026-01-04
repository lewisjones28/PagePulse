package com.page.pulse.orchestrator.service;

import com.page.pulse.domain.entity.document.Document;
import com.page.pulse.domain.entity.document.DocumentRuleViolation;
import com.page.pulse.domain.entity.Rule;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;
import com.page.pulse.orchestrator.repository.DocumentRuleViolationRepository;
import com.page.pulse.orchestrator.repository.RuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DocumentRuleViolationService}.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class DocumentRuleViolationServiceTest
{
    @Mock
    private DocumentRuleViolationRepository violationRepository;
    @Mock
    private RuleRepository ruleRepository;
    @InjectMocks
    private DocumentRuleViolationService violationService;
    private Document testDocument;
    private Rule testRule;
    private RuleEvaluation passingEvaluation;
    private RuleEvaluation failingEvaluation;

    @BeforeEach
    void setUp()
    {
        testDocument =
            new Document( "DOC-123", "owner-123", "Test Document", "CURRENT", java.util.Arrays.asList( "tag1", "tag2" ),
                java.time.LocalDateTime.now(), java.time.LocalDateTime.now() );
        testDocument.setId( 1L );
        testRule = new Rule( "TestRule", "Test rule description" );
        testRule.setId( 1 );
        passingEvaluation = new RuleEvaluation( "TestRule", RuleResult.pass( "DOC-123" ) );
        failingEvaluation = new RuleEvaluation( "TestRule", RuleResult.fail( "Test failure reason", "DOC-123" ) );
    }

    @Test
    void shouldCreateNewViolationWhenNoneExists()
    {
        // given
        when( ruleRepository.findByName( "TestRule" ) ).thenReturn( Optional.of( testRule ) );
        when( violationRepository.findByDocumentAndRule( testDocument, testRule ) ).thenReturn( Optional.empty() );

        final DocumentRuleViolation newViolation =
            new DocumentRuleViolation( testDocument, testRule, true, "Test failure reason" );
        when( violationRepository.save( any( DocumentRuleViolation.class ) ) ).thenReturn( newViolation );

        // when
        final DocumentRuleViolation result = violationService.saveOrUpdateViolation( testDocument, failingEvaluation );

        // then
        assertNotNull( result );
        verify( violationRepository ).findByDocumentAndRule( testDocument, testRule );
        verify( violationRepository ).save( any( DocumentRuleViolation.class ) );
    }

    @Test
    void shouldUpdateExistingViolation()
    {
        // given
        final DocumentRuleViolation existingViolation =
            new DocumentRuleViolation( testDocument, testRule, false, "Old message" );

        when( ruleRepository.findByName( "TestRule" ) ).thenReturn( Optional.of( testRule ) );
        when( violationRepository.findByDocumentAndRule( testDocument, testRule ) ).thenReturn(
            Optional.of( existingViolation ) );
        when( violationRepository.save( existingViolation ) ).thenReturn( existingViolation );

        // when
        final DocumentRuleViolation result = violationService.saveOrUpdateViolation( testDocument, failingEvaluation );

        // then
        assertNotNull( result );
        assertTrue( result.getViolating() );
        assertEquals( "Test failure reason", result.getViolationDetails() );
        verify( violationRepository ).save( existingViolation );
    }

    @Test
    void shouldReturnNullWhenRuleNotFound()
    {
        // given
        when( ruleRepository.findByName( "NonExistentRule" ) ).thenReturn( Optional.empty() );
        final RuleEvaluation evaluation = new RuleEvaluation( "NonExistentRule", RuleResult.fail( "Test", "DOC-123" ) );

        // when
        final DocumentRuleViolation result = violationService.saveOrUpdateViolation( testDocument, evaluation );

        // then
        assertNull( result );
        verify( violationRepository, never() ).save( any() );
    }

    @Test
    void shouldSetViolatingToFalseForPassingEvaluation()
    {
        // given
        final DocumentRuleViolation existingViolation =
            new DocumentRuleViolation( testDocument, testRule, true, "Previous violation" );

        // when
        when( ruleRepository.findByName( "TestRule" ) ).thenReturn( Optional.of( testRule ) );
        when( violationRepository.findByDocumentAndRule( testDocument, testRule ) ).thenReturn(
            Optional.of( existingViolation ) );
        when( violationRepository.save( existingViolation ) ).thenReturn( existingViolation );

        final DocumentRuleViolation result = violationService.saveOrUpdateViolation( testDocument, passingEvaluation );

        // then
        assertNotNull( result );
        assertFalse( result.getViolating() );
        assertEquals( "OK", result.getViolationDetails() );
        verify( violationRepository ).save( existingViolation );
    }

    @Test
    void shouldCheckIfDocumentViolatesRule()
    {
        // given
        when( ruleRepository.findByName( "TestRule" ) ).thenReturn( Optional.of( testRule ) );
        when( violationRepository.isDocumentViolatingRule( testDocument, testRule ) ).thenReturn( true );

        // when
        final boolean result = violationService.isDocumentViolatingRule( testDocument, "TestRule" );

        // then
        assertTrue( result );
        verify( violationRepository ).isDocumentViolatingRule( testDocument, testRule );
    }

    @Test
    void shouldReturnFalseWhenRuleNotFoundForViolationCheck()
    {
        // given
        when( ruleRepository.findByName( "NonExistentRule" ) ).thenReturn( Optional.empty() );

        // when
        final boolean result = violationService.isDocumentViolatingRule( testDocument, "NonExistentRule" );

        // then
        assertFalse( result );
        verify( violationRepository, never() ).isDocumentViolatingRule( any(), any() );
    }
}
