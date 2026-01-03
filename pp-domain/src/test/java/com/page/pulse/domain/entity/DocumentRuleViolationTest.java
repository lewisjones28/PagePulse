package com.page.pulse.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for the DocumentRuleViolation entity.
 *
 * @author lewisjones
 */
class DocumentRuleViolationTest
{
    private Document document;
    private Rule rule;
    private DocumentRuleViolation violation;

    @BeforeEach
    void setUp()
    {
        // Create a test document
        document = new Document( "DOC-123", "owner-123", "Test Document", "active",
                                List.of( "test", "document" ), LocalDateTime.now(), LocalDateTime.now() );

        // Create a test rule
        rule = new Rule( "Test Rule", "This is a test rule", true );

        // Create a test violation
        violation = new DocumentRuleViolation( document, rule, true, "Document violates test rule" );
    }

    @Test
    void shouldCreateDocumentRuleViolationWithAllFields()
    {
        assertThat( violation.getDocument() ).isEqualTo( document );
        assertThat( violation.getRule() ).isEqualTo( rule );
        assertThat( violation.getViolating() ).isTrue();
        assertThat( violation.getViolationDetails() ).isEqualTo( "Document violates test rule" );
    }

    @Test
    void shouldCreateDocumentRuleViolationWithoutDetails()
    {
        final DocumentRuleViolation violationWithoutDetails = new DocumentRuleViolation( document, rule, true );

        assertThat( violationWithoutDetails.getDocument() ).isEqualTo( document );
        assertThat( violationWithoutDetails.getRule() ).isEqualTo( rule );
        assertThat( violationWithoutDetails.getViolating() ).isTrue();
        assertThat( violationWithoutDetails.getViolationDetails() ).isNull();
    }

    @Test
    void shouldDefaultViolatingToFalseWhenNull()
    {
        final DocumentRuleViolation nonViolation = new DocumentRuleViolation( document, rule, null );

        assertThat( nonViolation.getViolating() ).isFalse();
    }

    @Test
    void shouldSetViolatingStatusCorrectly()
    {
        violation.setViolating( false );
        assertThat( violation.getViolating() ).isFalse();

        violation.setViolating( true );
        assertThat( violation.getViolating() ).isTrue();
    }

    @Test
    void shouldUpdateViolationDetails()
    {
        final String newDetails = "Updated violation details";
        violation.setViolationDetails( newDetails );
        assertThat( violation.getViolationDetails() ).isEqualTo( newDetails );
    }

    @Test
    void shouldHandleEqualsAndHashCodeBasedOnDocumentAndRule()
    {
        final DocumentRuleViolation sameViolation = new DocumentRuleViolation( document, rule, false );
        final DocumentRuleViolation differentViolation = new DocumentRuleViolation( document,
            new Rule( "Different Rule", "Different description" ), true );

        assertThat( violation ).isEqualTo( sameViolation );
        assertThat( violation ).isNotEqualTo( differentViolation );
        assertThat( violation.hashCode() ).isEqualTo( sameViolation.hashCode() );
    }

    @Test
    void shouldHandleToStringExcludingDocumentAndRule()
    {
        final String toStringResult = violation.toString();

        // Should not contain document or rule details due to @ToString(exclude = {"document", "rule"})
        assertThat( toStringResult ).doesNotContain( "Test Document" );
        assertThat( toStringResult ).doesNotContain( "Test Rule" );
        assertThat( toStringResult ).contains( "DocumentRuleViolation" );
    }

    @Test
    void shouldCreateProtectedNoArgConstructor()
    {
        // Test that the no-arg constructor exists and can be called (for JPA)
        final DocumentRuleViolation emptyViolation = new DocumentRuleViolation();
        assertThat( emptyViolation ).isNotNull();
    }
}
