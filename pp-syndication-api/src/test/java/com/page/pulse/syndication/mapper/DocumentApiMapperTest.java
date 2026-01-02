package com.page.pulse.syndication.mapper;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.syndication.model.DocumentApiDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
