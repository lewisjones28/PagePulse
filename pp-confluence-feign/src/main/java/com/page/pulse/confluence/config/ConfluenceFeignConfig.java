package com.page.pulse.confluence.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

/**
 * Feign configuration for Confluence API integration.
 *
 * @author lewisjones
 */
@Configuration
public class ConfluenceFeignConfig
{

    @Value( "${confluence.username}" )
    private String username;

    @Value( "${confluence.api-token}" )
    private String apiToken;

    @Bean
    public RequestInterceptor confluenceAuthInterceptor()
    {
        return template ->
        {
            final String auth = username.concat( ":" ).concat( apiToken );
            final String base64 = Base64.getEncoder().encodeToString( auth.getBytes() );
            template.header( "Authorization", "Basic " + base64 );
            template.header( "Accept", "application/json" );
        };
    }

}
