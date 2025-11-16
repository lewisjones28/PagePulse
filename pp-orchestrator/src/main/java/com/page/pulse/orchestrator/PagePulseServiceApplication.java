package com.page.pulse.orchestrator;

import com.page.pulse.confluence.client.ConfluencePageParams;
import com.page.pulse.orchestrator.service.ConfluenceApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application entry point.
 *
 * @author lewisjones
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableFeignClients( basePackages = "com.page.pulse.confluence.client" )
public class PagePulseServiceApplication
{

    private static final Logger log = LoggerFactory.getLogger( PagePulseServiceApplication.class );

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    static void main( final String[] args )
    {
        SpringApplication.run( PagePulseServiceApplication.class, args );
    }

    /**
     * Simple startup runner to test that the Confluence API client is wired and callable.
     *
     * @param confluenceApiService the Confluence API service
     * @return CommandLineRunner that performs the test
     */
    @Bean
    public CommandLineRunner confluenceInvocationTest( final ConfluenceApiService confluenceApiService )
    {
        return args ->
        {
            log.info( "Confluence invocation test started" );
            confluenceApiService.collectPages( new ConfluencePageParams() );
        };
    }

}
