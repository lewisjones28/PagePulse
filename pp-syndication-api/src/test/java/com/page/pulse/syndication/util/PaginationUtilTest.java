package com.page.pulse.syndication.util;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.syndication.model.DocumentApiDto;
import com.page.pulse.syndication.model.PagedDocumentApiDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PaginationUtil}.
 *
 * @author lewisjones
 */
class PaginationUtilTest
{

    @Test
    void testCreateSortOrderShouldCreateSortOrder()
    {
        // given
        final List<String> sortCriteria = List.of( "title,asc", "status,desc" );

        // when
        final List<Sort.Order> sortOrders = PaginationUtil.createSortOrder( sortCriteria );

        // then
        assertEquals( 2, sortOrders.size() );
        assertEquals( "title", sortOrders.get( 0 ).getProperty() );
        assertEquals( Sort.Direction.ASC, sortOrders.get( 0 ).getDirection() );
        assertEquals( "status", sortOrders.get( 1 ).getProperty() );
        assertEquals( Sort.Direction.DESC, sortOrders.get( 1 ).getDirection() );
    }

    @Test
    void testCreateSortOrderShouldReturnEmptyListForNullSort()
    {
        // when
        final List<Sort.Order> sortOrders = PaginationUtil.createSortOrder( null );

        // then
        assertTrue( sortOrders.isEmpty() );
    }

    @Test
    void testCreateSortOrderShouldReturnEmptyListForEmptySort()
    {
        // when
        final List<Sort.Order> sortOrders = PaginationUtil.createSortOrder( Collections.emptyList() );

        // then
        assertTrue( sortOrders.isEmpty() );
    }

    @Test
    void testCreateSortOrderShouldConvertToPagedDto()
    {
        // given
        final Document document =
            new Document( "ext-123", "owner-123", "Test Title", "ACTIVE", Collections.singletonList( "tag1" ),
                LocalDateTime.now(), LocalDateTime.now() );
        final Page<Document> page = new PageImpl<>( List.of( document ), PageRequest.of( 0, 10 ), 1 );
        final Function<Document, DocumentApiDto> mapper = doc ->
        {
            final DocumentApiDto dto = new DocumentApiDto();
            dto.setId( doc.getId() );
            dto.setTitle( doc.getTitle() );
            return dto;
        };

        // when
        final PagedDocumentApiDto pagedDto = PaginationUtil.toPagedDto( page, mapper );

        // then
        assertNotNull( pagedDto );
        assertEquals( 1, pagedDto.getContent().size() );
        assertEquals( document.getTitle(), pagedDto.getContent().getFirst().getTitle() );
        assertNotNull( pagedDto.getPageInfo() );
        assertEquals( 1, pagedDto.getPageInfo().getTotalPages() );
        assertEquals( 1, pagedDto.getPageInfo().getTotalElements() );
        assertEquals( 0, pagedDto.getPageInfo().getCurrentPage() );
    }
}

