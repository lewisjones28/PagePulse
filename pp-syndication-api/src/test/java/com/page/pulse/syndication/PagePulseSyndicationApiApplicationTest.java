package com.page.pulse.syndication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for PagePulseSyndicationApiApplication.
 *
 * @author lewisjones
 */
@SpringBootTest
@ActiveProfiles( "test" )
@AutoConfigureMockMvc
class PagePulseSyndicationApiApplicationTest
{

    @Test
    void contextLoads()
    {
        assertTrue( Boolean.TRUE );
    }

}
