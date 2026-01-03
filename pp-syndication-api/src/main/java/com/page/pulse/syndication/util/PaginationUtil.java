package com.page.pulse.syndication.util;

import com.page.pulse.syndication.model.PagedDocumentApiDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
     * Default page size for pagination.
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

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

        final com.page.pulse.syndication.model.PageInfo pageInfo = new com.page.pulse.syndication.model.PageInfo();
        pageInfo.setPages( page.getTotalPages() );
        pageInfo.setElements( page.getTotalElements() );
        pageInfo.setPage( page.getNumber() );
        pagedDto.setPageInfo( pageInfo );

        return pagedDto;
    }

    /**
     * Creates a PageInfo object from a Spring Data Page.
     *
     * @param page the Spring Data Page
     * @param <T>  the type of content in the page
     * @return PageInfo object containing pagination information
     */
    public static <T> com.page.pulse.syndication.model.PageInfo toPageInfo( final Page<T> page )
    {
        final com.page.pulse.syndication.model.PageInfo pageInfo = new com.page.pulse.syndication.model.PageInfo();
        pageInfo.setPages( page.getTotalPages() );
        pageInfo.setElements( page.getTotalElements() );
        pageInfo.setPage( page.getNumber() );
        return pageInfo;
    }

    /**
     * Creates a Pageable object from page, size, and sort parameters.
     *
     * @param page the page number (0-indexed)
     * @param size the page size
     * @param sort the sort criteria
     * @return Pageable object for use with Spring Data repositories
     */
    public static org.springframework.data.domain.Pageable createPageable( final Integer page, final Integer size, final List<String> sort )
    {
        final int pageNumber = page != null ? page : 0;
        final int pageSize = size != null ? size : DEFAULT_PAGE_SIZE;
        final List<Sort.Order> orders = createSortOrder( sort );

        if ( orders.isEmpty() )
        {
            return org.springframework.data.domain.PageRequest.of( pageNumber, pageSize );
        }

        return org.springframework.data.domain.PageRequest.of( pageNumber, pageSize, Sort.by( orders ) );
    }

    /**
     * Creates a list of Sort.Order objects from a list of sort criteria.
     *
     * @param sort list of sort criteria in the format "property,direction"
     * @return list of Sort.Order objects
     */
    public static List<Sort.Order> createSortOrder( final List<String> sort )
    {
        if ( sort == null || sort.isEmpty() )
        {
            return Collections.emptyList();
        }

        final List<String> tokens = sort.stream()
            .filter( s -> s != null && !s.isBlank() )
            .map( String::trim )
            .toList();

        if ( tokens.isEmpty() )
        {
            return Collections.emptyList();
        }

        // If any token contains a comma, assume the canonical OpenAPI format: "field,dir".
        if ( tokens.stream().anyMatch( s -> s.contains( "," ) ) )
        {
            return tokens.stream()
                .map( PaginationUtil::toSortOrder )
                .flatMap( Optional::stream )
                .collect( Collectors.toList() );
        }

        // Otherwise, treat tokens as pairs: field then optional direction.
        // e.g. ["title", "asc", "status", "desc"]
        final List<Sort.Order> orders = new java.util.ArrayList<>();
        for ( int i = 0; i < tokens.size(); i++ )
        {
            final String field = tokens.get( i );
            if ( field.isBlank() )
            {
                continue;
            }

            Sort.Direction direction = Sort.Direction.ASC;
            if ( i + 1 < tokens.size() )
            {
                final Optional<Sort.Direction> nextAsDirection = tryParseDirection( tokens.get( i + 1 ) );
                if ( nextAsDirection.isPresent() )
                {
                    direction = nextAsDirection.get();
                    i++; // consume direction token
                }
            }

            orders.add( new Sort.Order( direction, field ) );
        }

        return orders;
    }

    /**
     * Tries to parse a string value into a Sort.Direction.
     *
     * @param value the string value to parse
     * @return Optional containing the Sort.Direction if valid, or empty if invalid
     */
    private static Optional<Sort.Direction> tryParseDirection( final String value )
    {
        if ( value == null || value.isBlank() )
        {
            return Optional.empty();
        }

        try
        {
            return Optional.of( Sort.Direction.fromString( value.trim() ) );
        }
        catch ( final IllegalArgumentException ex )
        {
            return Optional.empty();
        }
    }

    /**
     * Converts a sort criteria string to a Sort.Order object.
     *
     * @param sortValue sort criteria in the format "property,direction"
     * @return Optional containing the Sort.Order object, or empty if invalid
     */
    private static Optional<Sort.Order> toSortOrder( final String sortValue )
    {
        final String trimmed = sortValue.trim();
        if ( trimmed.isEmpty() )
        {
            return Optional.empty();
        }

        final String[] parts = trimmed.split( ",", -1 );
        final String property = parts[ 0 ].trim();
        if ( property.isEmpty() )
        {
            return Optional.empty();
        }

        // "field" (defaults to ASC) or "field,asc|desc".
        final Sort.Direction direction;
        if ( parts.length < 2 || parts[ 1 ].isBlank() )
        {
            direction = Sort.Direction.ASC;
        }
        else
        {
            try
            {
                direction = Sort.Direction.fromString( parts[ 1 ].trim() );
            }
            catch ( final IllegalArgumentException ex )
            {
                // If a caller passes an invalid direction, ignore it rather than erroring.
                return Optional.empty();
            }
        }

        return Optional.of( new Sort.Order( direction, property ) );
    }
}
