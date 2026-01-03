package com.page.pulse.syndication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for PagePulse Syndication API.
 *
 * @author lewisjones
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer
{

    /**
     * Maximum age for CORS preflight requests (in seconds).
     */
    private static final int MAX_AGE = 3600;

    /**
     * Configure CORS mappings.
     *
     * @param registry the CorsRegistry to configure
     */
    @Override
    public void addCorsMappings( final CorsRegistry registry )
    {
        registry.addMapping( "/**" )
            .allowedOriginPatterns( "*" )  // Allow all origins for development
            .allowedMethods( "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH" )
            .allowedHeaders( "*" )
            .allowCredentials( true )
            .maxAge( MAX_AGE );
    }

    /**
     * Define the CORS configuration source bean.
     *
     * @return the CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        final CorsConfiguration configuration = new CorsConfiguration();

        // Allow all origins for development - configure specific origins for production
        configuration.setAllowCredentials( true );
        configuration.addAllowedOriginPattern( "*" );

        // Allow all headers
        configuration.addAllowedHeader( "*" );

        // Allow all HTTP methods
        configuration.addAllowedMethod( "GET" );
        configuration.addAllowedMethod( "POST" );
        configuration.addAllowedMethod( "PUT" );
        configuration.addAllowedMethod( "DELETE" );
        configuration.addAllowedMethod( "OPTIONS" );
        configuration.addAllowedMethod( "HEAD" );
        configuration.addAllowedMethod( "PATCH" );

        // Cache preflight response for 1 hour
        configuration.setMaxAge( ( long ) MAX_AGE );

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", configuration );

        return source;
    }
}
