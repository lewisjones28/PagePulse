package com.page.pulse.confluence.client;

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
     * @return the raw JSON response body for the pages request.
     */
    @RequestLine( "GET /pages" )
    String getPages();

    /**
     * Retrieves pages from the Confluence API with query parameters.
     *
     * @param queryMap the query parameters to include in the request.
     * @return the raw JSON response body for the pages request.
     */
    @RequestLine( "GET /pages" )
    String getPages( @QueryMap Map<String, Object> queryMap );

    /**
     * Retrieves a specific page from the Confluence API by its ID.
     *
     * @param pageId   the ID of the page to retrieve.
     * @param queryMap the query parameters to include in the request.
     * @return the raw JSON response body for the page request.
     */
    @RequestLine( "GET /pages/{pageId}" )
    String getPage( @Param( "pageId" ) String pageId, @QueryMap Map<String, Object> queryMap );

}
