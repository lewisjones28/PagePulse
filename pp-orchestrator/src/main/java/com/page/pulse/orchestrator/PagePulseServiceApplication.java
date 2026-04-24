package com.page.pulse.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
@EntityScan( basePackages = "com.page.pulse.domain" )
@EnableJpaRepositories( basePackages = "com.page.pulse.orchestrator.repository" )
@EnableJpaAuditing
public class PagePulseServiceApplication
{

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    static void main( final String[] args )
    {
        SpringApplication.run( PagePulseServiceApplication.class, args );
    }

}
