package com.page.pulse.confluence.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.confluence.config.ConfluenceFeignConfig;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

/**
 * Feign client for interacting with the Confluence API.
 *
 * @author lewisjones
 */
@FeignClient( name = "confluenceApi", url = "${confluence.base-url}/wiki/api/v2",
              configuration = ConfluenceFeignConfig.class )
public interface ConfluenceApiClient
{

    /**
     * Retrieves pages from the Confluence API without any query parameters.
     *
     * @return JsonNode representing the pages.
     */
    @RequestLine( "GET /pages" )
    JsonNode getPages();

    /**
     * Retrieves pages from the Confluence API with query parameters.
     *
     * @param queryMap the query parameters to include in the request.
     * @return JsonNode representing the pages.
     */
    @RequestLine( "GET /pages" )
    JsonNode getPages( @QueryMap Map<String, Object> queryMap );

    /**
     * Retrieves a specific page from the Confluence API by its ID.
     *
     * @param pageId   the ID of the page to retrieve.
     * @param queryMap the query parameters to include in the request.
     * @return JsonNode representing the page.
     */
    @RequestLine( "GET /pages/{pageId}" )
    JsonNode getPage( @Param( "pageId" ) String pageId, @QueryMap Map<String, Object> queryMap );

}
