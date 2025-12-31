package com.page.pulse.orchestrator.service;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DocumentService}.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class DocumentServiceTest
{
    @Mock
    private DocumentRepository repository;
    @InjectMocks
    private DocumentService service;
    private DocumentDto dto;

    @BeforeEach
    void setUp()
    {
        dto =
            new DocumentDto( "ext-1", "owner", "title", "status", List.of( "tag" ), LocalDateTime.now().minusDays( 1 ),
                LocalDateTime.now() );
    }

    @Test
    void testSaveNewDocumentWhenExternalIdNotFound()
    {
        // when
        when( repository.findByExternalId( "ext-1" ) ).thenReturn( Optional.empty() );

        service.saveOrUpdate( dto );

        // then
        final ArgumentCaptor<Document> captor = ArgumentCaptor.forClass( Document.class );
        verify( repository ).save( captor.capture() );
        final Document saved = captor.getValue();
        assertThat( saved.getExternalId() ).isEqualTo( "ext-1" );
        assertThat( saved.getTags() ).containsExactly( "tag" );
    }

    @Test
    void testUpdateExistingDocument()
    {
        // given
        final Document existing =
            new Document( "ext-1", "old", "old-title", "old-status", List.of(), LocalDateTime.now().minusDays( 2 ),
                LocalDateTime.now() );
        existing.setId( 5L );

        // when
        when( repository.findByExternalId( "ext-1" ) ).thenReturn( Optional.of( existing ) );

        service.saveOrUpdate( dto );

        // then
        assertThat( existing.getExternalOwnerId() ).isEqualTo( "owner" );
        assertThat( existing.getTitle() ).isEqualTo( "title" );
        assertThat( existing.getStatus() ).isEqualTo( "status" );
        assertThat( existing.getTags() ).containsExactly( "tag" );
        verify( repository, never() ).save( existing );
    }
}
