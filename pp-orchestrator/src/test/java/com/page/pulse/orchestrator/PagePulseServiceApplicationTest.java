package com.page.pulse.orchestrator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link PagePulseServiceApplication} tests.
 *
 * @author lewisjones
 */
@SpringBootTest
@ActiveProfiles( "test" )
class PagePulseServiceApplicationTest
{

    @Test
    void contextLoads()
    {
        assertTrue( Boolean.TRUE );
    }

}
