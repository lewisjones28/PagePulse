package com.page.pulse.confluence.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.confluence.config.ConfluenceFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

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
     * Retrieves pages from the Confluence API.
     *
     * @return JsonNode representing the pages.
     */
    @GetMapping( "/pages" )
    JsonNode getPages();

}
