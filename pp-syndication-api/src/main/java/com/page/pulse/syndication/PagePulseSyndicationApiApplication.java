package com.page.pulse.syndication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application entry point.
 *
 * @author lewisjones
 */
@SpringBootApplication
@EnableJpaRepositories( basePackages = "com.page.pulse.syndication.repository" )
@EntityScan( basePackages = "com.page.pulse.domain.entity" )
@EnableJpaAuditing
public class PagePulseSyndicationApiApplication
{

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    static void main( final String[] args )
    {
        SpringApplication.run( PagePulseSyndicationApiApplication.class, args );
    }

}
