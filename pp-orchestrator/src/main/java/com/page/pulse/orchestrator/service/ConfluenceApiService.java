package com.page.pulse.orchestrator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.confluence.client.ConfluenceApiClient;
import com.page.pulse.confluence.client.ConfluencePageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service that wraps the Feign client for Confluence API calls.
 *
 * @author lewisjones
 */
@Service
public class ConfluenceApiService
{

    private static final Logger log = LoggerFactory.getLogger( ConfluenceApiService.class );
    private final ConfluenceApiClient confluenceApiClient;

    /**
     * Constructor for ConfluenceApiService.
     *
     * @param confluenceApiClient the Feign client for Confluence API
     */
    public ConfluenceApiService( final ConfluenceApiClient confluenceApiClient )
    {
        this.confluenceApiClient = confluenceApiClient;
    }

    /**
     * Retrieve pages from Confluence via the Feign client.
     *
     * @return JsonNode response from the Confluence API (may be null)
     */
    private JsonNode getPages()
    {
        return getPages( new ConfluencePageParams() );
    }

    /**
     * Retrieve pages using a parameter object which is converted to a query map.
     *
     * @param params optional query params to influence the listing request
     * @return JsonNode response from the Confluence API (may be null)
     */
    private JsonNode getPages( final ConfluencePageParams params )
    {
        try
        {
            if ( params == null || params.isEmpty() )
            {
                return confluenceApiClient.getPages();
            }
        }
        catch ( final Exception e )
        {
            log.error( "Error fetching pages from Confluence API", e );
            throw e;
        }

        return null;
    }

    /**
     * Collect pages by fetching the pages list then retrieving each page's details.
     *
     * @param params optional query params to influence the listing request
     */
    public void collectPages( final ConfluencePageParams params )
    {
        log.info( "Collecting Confluence pages with params: {}", params );
        try
        {
            final JsonNode pages = getPages( params );
            if ( pages == null || pages.isNull() )
            {
                log.warn( "No pages returned from Confluence API" );
                return;
            }

            // pages may be an object containing arrays or an array of pages; iterate defensively
            final var outer = pages.elements();
            while ( outer.hasNext() )
            {
                final JsonNode pageList = outer.next();
                if ( pageList == null || pageList.isNull() )
                {
                    continue;
                }

                if ( pageList.isArray() )
                {
                    for ( final JsonNode page : pageList )
                    {
                        if ( page == null || page.isNull() )
                        {
                            log.warn( "Encountered null page in list" );
                            continue;
                        }

                        final JsonNode idNode = page.get( "id" );
                        if ( idNode == null || idNode.isNull() )
                        {
                            log.warn( "Page missing 'id' field: {}", page );
                            continue;
                        }

                        final String pageId = idNode.asText();
                        try
                        {
                            final JsonNode pageWithTags = getPage( pageId );
                            log.debug( "Retrieved page {}: {}", pageId, pageWithTags );
                        }
                        catch ( final Exception e )
                        {
                            log.warn( "Failed to fetch page {}", pageId, e );
                        }
                    }
                }
            }
        }
        catch ( final Exception e )
        {
            log.warn( "Call failed while collecting pages", e );
        }
    }

    /**
     * Retrieve a specific page from Confluence via the Feign client.
     *
     * @param pageId the ID of the page to retrieve
     * @return JsonNode response from the Confluence API (may be null)
     */
    public JsonNode getPage( final String pageId )
    {
        try
        {
            final ConfluencePageParams params = new ConfluencePageParams().includeLabels( Boolean.TRUE );
            return confluenceApiClient.getPage( pageId, params.toMap() );
        }
        catch ( final Exception e )
        {
            log.error( "Error fetching page with ID {} from Confluence API", pageId, e );
            throw e;
        }
    }
}
