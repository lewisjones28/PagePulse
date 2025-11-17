package com.page.pulse.orchestrator.mapper.impl;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.page.pulse.orchestrator.pojo.Document;

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
        JsonNode nullNode = objectMapper.nullNode();
        assertThat( mapper.mapNode( nullNode ) ).isNull();
    }

    @Test
    void testMapNodeWhenMinimalValidPage() throws Exception
    {
        String json = """
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
        JsonNode node = objectMapper.readTree( json );
        Document doc = mapper.mapNode( node );
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
        String json = """
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
        JsonNode node = objectMapper.readTree( json );
        Document doc = mapper.mapNode( node );
        assertThat( doc.tags() ).containsExactly( "tag1", "tag2" );
    }

    @Test
    void testMapNodeWhenLabelsMissingOrEmpty() throws Exception
    {
        String jsonNoLabels = """
            {
            	"id": "abc123",
            	"ownerId": "owner1",
            	"title": "Test Page",
            	"status": "current",
            	"createdAt": "2023-01-01T10:00:00Z",
            	"version": { "createdAt": "2023-01-02T12:00:00Z" }
            }
            """;
        JsonNode nodeNoLabels = objectMapper.readTree( jsonNoLabels );
        Document docNoLabels = mapper.mapNode( nodeNoLabels );
        assertThat( docNoLabels.tags() ).isEmpty();

        String jsonEmptyResults = """
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
        JsonNode nodeEmptyResults = objectMapper.readTree( jsonEmptyResults );
        Document docEmptyResults = mapper.mapNode( nodeEmptyResults );
        assertThat( docEmptyResults.tags() ).isEmpty();
    }
}
