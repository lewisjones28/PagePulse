package com.page.pulse.orchestrator.mapper.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.page.pulse.orchestrator.pojo.Document;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ConfluenceDocumentMapper}.
 *
 * @author lewisjones
 */
class ConfluenceDocumentMapperTest
{
    private final ConfluenceDocumentMapper mapper = new ConfluenceDocumentMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testMapNodeWhenNodeIsNull()
    {
        assertThat( mapper.mapNode( null ) ).isNull();
    }

    @Test
    void testMapNodeWhenNodeIsJsonNull()
    {
       final JsonNode nullNode = objectMapper.nullNode();
       assertThat( mapper.mapNode( nullNode ) ).isNull();
    }

    @Test
    void testMapNodeWhenMinimalValidPage() throws Exception
    {
       // given
       final String json = """
            {
            "id": "abc123",
            "ownerId": "owner1",
            "title": "Test Page",
            "status": "current",
            "createdAt": "2023-01-01T10:00:00Z",
            "version": {
            "createdAt": "2023-01-02T12:00:00Z"
            },
            "labels": { "results": [] }
         }
            """;
       final JsonNode node = objectMapper.readTree( json );

        // when
        final Document doc = mapper.mapNode( node );

        // then
        assertThat( doc.externalId() ).isEqualTo( "abc123" );
        assertThat( doc.externalOwnerId() ).isEqualTo( "owner1" );
        assertThat( doc.title() ).isEqualTo( "Test Page" );
        assertThat( doc.status() ).isEqualTo( "current" );
        assertThat( doc.tags() ).isEmpty();
        assertThat( doc.createdAt() ).isInstanceOf( LocalDateTime.class );
        assertThat( doc.updatedAt() ).isInstanceOf( LocalDateTime.class );
    }

    @Test
    void testMapNodeWhenTagsPresentInLabels() throws Exception
    {
        // given
        final String json = """
            {
            "id": "abc123",
            "ownerId": "owner1",
            "title": "Test Page",
            "status": "current",
            "createdAt": "2023-01-01T10:00:00Z",
            "version": { "createdAt": "2023-01-02T12:00:00Z" },
            "labels": {
            "results": [
            { "name": "tag1" },
            { "name": "tag2" }
            ]
            }
           }
           """;
        final JsonNode node = objectMapper.readTree( json );

        // when
        final Document doc = mapper.mapNode( node );

        // then
        assertThat( doc.tags() ).containsExactly( "tag1", "tag2" );
    }

    @Test
    void testMapNodeWhenLabelsMissingOrEmpty() throws Exception
    {
        final String jsonNoLabels = """
           {
            "id": "abc123",
            "ownerId": "owner1",
            "title": "Test Page",
            "status": "current",
            "createdAt": "2023-01-01T10:00:00Z",
            "version": { "createdAt": "2023-01-02T12:00:00Z" }
            }
           """;
        final JsonNode nodeNoLabels = objectMapper.readTree( jsonNoLabels );

        // when
        final Document docNoLabels = mapper.mapNode( nodeNoLabels );

        // then
        assertThat( docNoLabels.tags() ).isEmpty();

        final String jsonEmptyResults = """
            {
            "id": "abc123",
            "ownerId": "owner1",
            "title": "Test Page",
            "status": "current",
            "createdAt": "2023-01-01T10:00:00Z",
            "version": { "createdAt": "2023-01-02T12:00:00Z" },
            "labels": { "results": [] }
            }
            """;
        final JsonNode nodeEmptyResults = objectMapper.readTree( jsonEmptyResults );

        // when
        final Document docEmptyResults = mapper.mapNode( nodeEmptyResults );

        // then
        assertThat( docEmptyResults.tags() ).isEmpty();
    }
}
