package com.page.pulse.syndication.service;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.syndication.repository.DocumentApiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link DocumentApiService}.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class DocumentApiServiceTest
{

    @Mock
    private DocumentApiRepository documentApiRepository;
    @InjectMocks
    private DocumentApiService documentApiService;

    @Test
    void testGetDocumentsShouldGetDocuments()
    {
        // given
        final Pageable pageable = PageRequest.of( 0, 10 );
        final Document document =
            new Document( "ext-123", "owner-123", "Test Title", "ACTIVE", Collections.singletonList( "tag1" ),
                LocalDateTime.now(), LocalDateTime.now() );
        final Page<Document> expectedPage = new PageImpl<>( List.of( document ), pageable, 1 );

        // when
        when( documentApiRepository.findAll( pageable ) ).thenReturn( expectedPage );

        final Page<Document> actualPage = documentApiService.getDocuments( pageable );

        // then
        assertEquals( expectedPage, actualPage );
    }

    @Test
    void testGetDocumentsShouldReturnEmptyPageWhenNoDocuments()
    {
        // given
        final Pageable pageable = PageRequest.of( 0, 10 );
        final Page<Document> emptyPage = new PageImpl<>( Collections.emptyList(), pageable, 0 );

        // when
        when( documentApiRepository.findAll( pageable ) ).thenReturn( emptyPage );

        final Page<Document> actualPage = documentApiService.getDocuments( pageable );

        // then
        assertEquals( emptyPage, actualPage );
        assertEquals( 0, actualPage.getTotalElements() );
        assertEquals( 0, actualPage.getContent().size() );
    }
}
