package com.page.pulse.syndication.api;

import com.page.pulse.domain.entity.document.Document;
import com.page.pulse.syndication.repository.DocumentApiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link DocumentsApiImpl}.
 *
 * @author lewisjones
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles( "test" )
class DocumentsApiImplTest
{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DocumentApiRepository documentApiRepository;
    @BeforeEach
    void setUp()
    {
        documentApiRepository.deleteAll();
    }

    @Test
    void testGetDocumentsShouldGetDocuments() throws Exception
    {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Document document1 = new Document( "ext-1", "owner-1", "Title 1", "ACTIVE", List.of( "tag1" ), now, now );
        final Document document2 = new Document( "ext-2", "owner-2", "Title 2", "INACTIVE", List.of( "tag2" ), now, now );

        // when & then
        documentApiRepository.saveAll( List.of( document1, document2 ) );

        mockMvc.perform( get( "/documents" ).param( "page", "0" ).param( "size", "10" ).param( "sort", "title,asc" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.content.length()" ).value( 2 ) )
            .andExpect( jsonPath( "$.content[0].title" ).value( "Title 1" ) )
            .andExpect( jsonPath( "$.content[1].title" ).value( "Title 2" ) )
            .andExpect( jsonPath( "$.pageInfo.pages" ).value( 1 ) )
            .andExpect( jsonPath( "$.pageInfo.elements" ).value( 2 ) )
            .andExpect( jsonPath( "$.pageInfo.page" ).value( 0 ) );
    }
}

