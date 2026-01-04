package com.page.pulse.syndication.mapper;

import com.page.pulse.domain.entity.document.Document;
import com.page.pulse.domain.entity.document.DocumentRuleViolation;
import com.page.pulse.domain.entity.Rule;
import com.page.pulse.syndication.model.DocumentApiDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DocumentApiMapper}.
 *
 * @author lewisjones
 */
class DocumentApiMapperTest
{

    private final DocumentApiMapper mapper = DocumentApiMapper.INSTANCE;

    @Test
    void testToDtoShouldMapDocumentToDocumentApiDto()
    {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Document document =
            new Document( "ext-123", "owner-123", "Test Title", "ACTIVE", List.of( "tag1", "tag2" ), now, now );
        document.setId( 1L );

        // when
        final DocumentApiDto dto = mapper.toDto( document );

        // then
        assertNotNull( dto );
        assertEquals( document.getId(), dto.getId() );
        assertEquals( document.getExternalId(), dto.getExternalId() );
        assertEquals( document.getTitle(), dto.getTitle() );
        assertEquals( document.getStatus(), dto.getStatus() );
        assertEquals( document.getTags(), dto.getTags() );
        assertEquals( document.getDocumentLastCreatedAt().atOffset( ZoneOffset.UTC ), dto.getDocumentLastCreatedAt() );
        assertEquals( document.getDocumentLastUpdatedAt().atOffset( ZoneOffset.UTC ), dto.getDocumentLastUpdatedAt() );
    }

    @Test
    void testToDtoWithViolationsShouldMapDocumentWithViolations()
    {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Document document =
            new Document( "ext-123", "owner-123", "Test Title", "ACTIVE", List.of( "tag1", "tag2" ), now, now );
        document.setId( 1L );

        final Rule rule1 = new Rule( "TestRule1", "Test rule 1 description", true );
        rule1.setId( 1 );

        final Rule rule2 = new Rule( "TestRule2", "Test rule 2 description", true );
        rule2.setId( 2 );

        final DocumentRuleViolation violation1 =
            new DocumentRuleViolation( document, rule1, true, "Document violates rule 1" );
        violation1.setId( 1L );
        violation1.setCreatedOn( now.minusDays( 1 ) );
        violation1.setUpdatedOn( now );

        final DocumentRuleViolation violation2 =
            new DocumentRuleViolation( document, rule2, false, "Document passes rule 2" );
        violation2.setId( 2L );
        violation2.setCreatedOn( now.minusDays( 1 ) );
        violation2.setUpdatedOn( now );

        final List<DocumentRuleViolation> violations = Arrays.asList( violation1, violation2 );

        // when
        final DocumentApiDto dto = mapper.toDtoWithViolations( document, violations );

        // then
        assertNotNull( dto );
        assertEquals( document.getId(), dto.getId() );
        assertEquals( document.getExternalId(), dto.getExternalId() );
        assertEquals( document.getTitle(), dto.getTitle() );
        assertEquals( document.getStatus(), dto.getStatus() );
        assertEquals( document.getTags(), dto.getTags() );

        // Verify rule violations
        assertNotNull( dto.getRuleViolations() );
        assertEquals( 2, dto.getRuleViolations().size() );

        // First violation
        assertEquals( 1L, dto.getRuleViolations().getFirst().getId() );
        assertTrue( dto.getRuleViolations().getFirst().getViolating() );
        assertEquals( "Document violates rule 1", dto.getRuleViolations().getFirst().getViolationDetails() );
        assertEquals( "TestRule1", dto.getRuleViolations().getFirst().getRule().getName() );
        assertEquals( violation1.getCreatedOn().atOffset( ZoneOffset.UTC ),
            dto.getRuleViolations().getFirst().getCreatedAt() );
        assertEquals( violation1.getUpdatedOn().atOffset( ZoneOffset.UTC ),
            dto.getRuleViolations().getFirst().getUpdatedAt() );
        assertEquals( violation1.getUpdatedOn().atOffset( ZoneOffset.UTC ),
            dto.getRuleViolations().get( 0 ).getUpdatedAt() );

        // Second violation
        assertEquals( 2L, dto.getRuleViolations().get( 1 ).getId() );
        assertFalse( dto.getRuleViolations().get( 1 ).getViolating() );
        assertEquals( "Document passes rule 2", dto.getRuleViolations().get( 1 ).getViolationDetails() );
        assertEquals( "TestRule2", dto.getRuleViolations().get( 1 ).getRule().getName() );
    }

    @Test
    void testToDtoWithViolationsShouldMapDocumentWithNullViolations()
    {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Document document =
            new Document( "ext-124", "owner-124", "Test Title 2", "ACTIVE", List.of( "tag1" ), now, now );
        document.setId( 2L );

        // when
        final DocumentApiDto dto = mapper.toDtoWithViolations( document, null );

        // then
        assertNotNull( dto );
        assertEquals( document.getId(), dto.getId() );
        assertEquals( document.getExternalId(), dto.getExternalId() );
        assertEquals( document.getTitle(), dto.getTitle() );
        assertEquals( document.getStatus(), dto.getStatus() );
        assertEquals( document.getTags(), dto.getTags() );

        // Should have empty list when violations is null
        assertNotNull( dto.getRuleViolations() );
        assertTrue( dto.getRuleViolations().isEmpty() );
    }

    @Test
    void testToDtoWithViolationsShouldMapDocumentWithEmptyViolations()
    {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Document document =
            new Document( "ext-125", "owner-125", "Test Title 3", "ACTIVE", List.of( "tag1" ), now, now );
        document.setId( 3L );

        final List<DocumentRuleViolation> emptyViolations = Collections.emptyList();

        // when
        final DocumentApiDto dto = mapper.toDtoWithViolations( document, emptyViolations );

        // then
        assertNotNull( dto );
        assertEquals( document.getId(), dto.getId() );
        assertEquals( document.getExternalId(), dto.getExternalId() );
        assertEquals( document.getTitle(), dto.getTitle() );
        assertEquals( document.getStatus(), dto.getStatus() );
        assertEquals( document.getTags(), dto.getTags() );

        // Should have empty list when violations list is empty
        assertNotNull( dto.getRuleViolations() );
        assertTrue( dto.getRuleViolations().isEmpty() );
    }

    @Test
    void testToDtoWithViolationsShouldMapViolationWithNullRule()
    {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Document document =
            new Document( "ext-126", "owner-126", "Test Title 4", "ACTIVE", List.of( "tag1" ), now, now );
        document.setId( 4L );

        final DocumentRuleViolation violation =
            new DocumentRuleViolation( document, null, true, "Violation with null rule" );
        violation.setId( 1L );
        violation.setCreatedOn( now );
        violation.setUpdatedOn( now );

        final List<DocumentRuleViolation> violations = List.of( violation );

        // when
        final DocumentApiDto dto = mapper.toDtoWithViolations( document, violations );

        // then
        assertNotNull( dto );
        assertNotNull( dto.getRuleViolations() );
        assertEquals( 1, dto.getRuleViolations().size() );

        // Violation should be mapped but rule should be null
        assertEquals( 1L, dto.getRuleViolations().getFirst().getId() );
        assertTrue( dto.getRuleViolations().getFirst().getViolating() );
        assertEquals( "Violation with null rule", dto.getRuleViolations().getFirst().getViolationDetails() );
        assertNull( dto.getRuleViolations().getFirst().getRule() );
    }

    @Test
    void testToDtoWithViolationsShouldMapViolationWithNullTimestamps()
    {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Document document =
            new Document( "ext-127", "owner-127", "Test Title 5", "ACTIVE", List.of( "tag1" ), now, now );
        document.setId( 5L );

        final Rule rule = new Rule( "TestRule", "Test rule description", true );
        rule.setId( 1 );

        final DocumentRuleViolation violation =
            new DocumentRuleViolation( document, rule, false, "Violation with null timestamps" );
        violation.setId( 1L );
        // Leave createdOn and updatedOn as null

        final List<DocumentRuleViolation> violations = List.of( violation );

        // when
        final DocumentApiDto dto = mapper.toDtoWithViolations( document, violations );

        // then
        assertNotNull( dto );
        assertNotNull( dto.getRuleViolations() );
        assertEquals( 1, dto.getRuleViolations().size() );

        // Timestamps should be null when source timestamps are null
        assertEquals( 1L, dto.getRuleViolations().getFirst().getId() );
        assertFalse( dto.getRuleViolations().getFirst().getViolating() );
        assertEquals( "Violation with null timestamps", dto.getRuleViolations().getFirst().getViolationDetails() );
        assertNull( dto.getRuleViolations().getFirst().getCreatedAt() );
        assertNull( dto.getRuleViolations().getFirst().getUpdatedAt() );
        assertNotNull( dto.getRuleViolations().getFirst().getRule() );
        assertEquals( "TestRule", dto.getRuleViolations().getFirst().getRule().getName() );
    }
}
