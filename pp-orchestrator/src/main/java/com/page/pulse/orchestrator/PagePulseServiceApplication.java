package com.page.pulse.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
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

    static void main( final String[] args )
    {
        SpringApplication.run( PagePulseServiceApplication.class, args );
    }

}
