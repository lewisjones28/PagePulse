package com.page.pulse.orchestrator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.confluence.client.ConfluenceApiClient;
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
    public JsonNode getPages()
    {
        try
        {
            return confluenceApiClient.getPages();
        }
        catch ( final Exception e )
        {
            log.error( "Error fetching pages from Confluence API", e );
            throw e;
        }
    }
}
