package com.page.pulse.orchestrator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.confluence.client.ConfluenceApiClient;
import com.page.pulse.confluence.client.ConfluencePageParams;
import com.page.pulse.orchestrator.mapper.DocumentMapper;
import com.page.pulse.orchestrator.pojo.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
    private final DocumentMapper documentMapper;

    /**
     * Constructor for ConfluenceApiService.
     *
     * @param confluenceApiClient the Feign client for Confluence API
     * @param documentMapper the MapStruct mapper to convert JsonNode to PageDto
     */
    public ConfluenceApiService( final ConfluenceApiClient confluenceApiClient, final DocumentMapper documentMapper )
    {
        this.confluenceApiClient = confluenceApiClient;
        this.documentMapper = documentMapper;
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
            final ConfluencePageParams finalParams = ( params == null ) ? ConfluencePageParams.empty() : params;
            if ( finalParams.isEmpty() )
            {
                return confluenceApiClient.getPages();
            }
            else
            {
                return confluenceApiClient.getPages( finalParams.toMap() );
            }

        }
        catch ( final Exception e )
        {
            log.error( "Error fetching pages from Confluence API", e );
            throw e;
        }
    }

    /**
     * Collect pages by fetching the pages list then retrieving each page's details.
     *
     * @param params optional query params to influence the listing request
     * @return list of Document records (may be empty)
     */
    public List<Document> collectPages( final ConfluencePageParams params )
    {
        // Normalize params so callers can pass null safely; then ensure 'current' status.
        final ConfluencePageParams finalParams = ( params == null ) ? ConfluencePageParams.empty() : params;
        log.debug( "Collecting Confluence pages with params: {}", finalParams );
        try
        {
            final JsonNode pages = getPages( finalParams );
            if ( pages == null || pages.isNull() )
            {
                log.warn( "No pages returned from Confluence API" );
                return Collections.emptyList();
            }

            final List<JsonNode> pageWithDetails = pages.findValues( "id" ).stream()
                .map( idNode -> getPage( idNode.asText() ) )
                .toList();

            return documentMapper.toDocumentList( pageWithDetails );
        }
        catch ( final Exception e )
        {
            log.warn( "Call failed while collecting pages", e );
            return Collections.emptyList();
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
            final ConfluencePageParams params = ConfluencePageParams.empty().includeLabels( Boolean.TRUE );
            return confluenceApiClient.getPage( pageId, params.toMap() );
        }
        catch ( final Exception e )
        {
            log.error( "Error fetching page with ID {} from Confluence API", pageId, e );
            throw e;
        }
    }
}
