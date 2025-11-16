package com.page.pulse.orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application entry point.
 *
 * @author lewisjones
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
@EnableCaching
public class PagePulseServiceApplication
{

    static void main( final String[] args )
    {
        SpringApplication.run( PagePulseServiceApplication.class, args );
    }

}
