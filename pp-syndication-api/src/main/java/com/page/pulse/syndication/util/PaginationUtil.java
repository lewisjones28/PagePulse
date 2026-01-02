package com.page.pulse.syndication.util;

import com.page.pulse.syndication.model.PagedDocumentApiDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for pagination-related operations.
 *
 * @author lewisjones
 */
public final class PaginationUtil
{

    /**
     * Private constructor to prevent instantiation.
     */
    private PaginationUtil()
    {
    }

    /**
     * Converts a Page of entities to a PagedDocumentApiDto.
     *
     * @param page          the Page of entities
     * @param contentMapper function to map entities to DocumentApiDto
     * @param <T>           the type of the entity
     * @param <D>           the type of the DocumentApiDto
     * @return PagedDocumentApiDto containing the mapped content and pagination information
     */
    public static <T, D> PagedDocumentApiDto toPagedDto( final Page<T> page, final Function<T, D> contentMapper )
    {
        final PagedDocumentApiDto pagedDto = new PagedDocumentApiDto();
        pagedDto.setContent( page.getContent()
            .stream()
            .map( item -> ( com.page.pulse.syndication.model.DocumentApiDto ) contentMapper.apply( item ) )
            .collect( Collectors.toList() ) );
        pagedDto.setPageable( null ); // TODO: Map pageable
        pagedDto.setTotalPages( page.getTotalPages() );
        pagedDto.setTotalElements( page.getTotalElements() );
        pagedDto.setLast( page.isLast() );
        pagedDto.setSize( page.getSize() );
        pagedDto.setNumber( page.getNumber() );
        pagedDto.setSort( null ); // TODO: Map sort
        pagedDto.setNumberOfElements( page.getNumberOfElements() );
        pagedDto.setFirst( page.isFirst() );
        pagedDto.setEmpty( page.isEmpty() );
        return pagedDto;
    }

    /**
     * Creates a list of Sort.Order objects from a list of sort criteria.
     *
     * @param sort list of sort criteria in the format "property,direction"
     * @return list of Sort.Order objects
     */
    public static List<Sort.Order> createSortOrder( final List<String> sort )
    {
        if ( sort == null )
        {
            return Collections.emptyList();
        }
        return sort.stream().map( s ->
        {
            final String[] parts = s.split( "," );
            return new Sort.Order( Sort.Direction.fromString( parts[ 1 ] ), parts[ 0 ] );
        } ).collect( Collectors.toList() );
    }
}
