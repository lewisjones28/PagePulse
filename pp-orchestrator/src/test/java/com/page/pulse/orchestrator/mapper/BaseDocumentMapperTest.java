package com.page.pulse.orchestrator.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for {@link BaseDocumentMapper}.
 *
 * @author lewisjones
 */
class BaseDocumentMapperTest
{
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Simple mapper for testing that returns the node's text value as a string
    static class StringNodeMapper extends BaseDocumentMapper<String>
    {
        @Override
        protected String mapNode( final JsonNode node )
        {
            return node == null || node.isNull() ? null : node.asText();
        }
    }

    private final StringNodeMapper mapper = new StringNodeMapper();

    @Test
    void testToDocumentListWhenPagesNodeIsNull()
    {
        assertThat( mapper.toDocumentList( ( JsonNode ) null ) ).isEmpty();
    }

    @Test
    void testToDocumentListWhenPagesNodeIsJsonNull()
    {
        assertThat( mapper.toDocumentList( objectMapper.nullNode() ) ).isEmpty();
    }

    @Test
    void testToDocumentListWhenResultsArrayPresent() throws Exception
    {
        String json = """
            {
              "results": ["a", "b", "c"]
            }
            """;
        JsonNode node = objectMapper.readTree( json );
        List<String> result = mapper.toDocumentList( node );
        assertThat( result ).containsExactly( "a", "b", "c" );
    }

    @Test
    void testToDocumentListWhenResultsMissingUsesRoot() throws Exception
    {
        String json = "[\"a\", \"b\"]";
        JsonNode node = objectMapper.readTree( json );
        List<String> result = mapper.toDocumentList( node );
        assertThat( result ).containsExactly( "a", "b" );
    }

    @Test
    void testToDocumentListWhenResultsEmpty() throws Exception
    {
        String json = "{\"results\": []}";
        JsonNode node = objectMapper.readTree( json );
        List<String> result = mapper.toDocumentList( node );
        assertThat( result ).isEmpty();
    }

    @Test
    void testToDocumentListWithListInput() throws Exception
    {
        String json = "[\"x\", \"y\"]";
        JsonNode node = objectMapper.readTree( json );
        List<JsonNode> nodeList = List.of( node.get( 0 ), node.get( 1 ) );
        List<String> result = mapper.toDocumentList( nodeList );
        assertThat( result ).containsExactly( "x", "y" );
    }

    @Test
    void testToDocumentListWithNullOrEmptyList()
    {
        assertThat( mapper.toDocumentList( ( List<JsonNode> ) null ) ).isEmpty();
        assertThat( mapper.toDocumentList( List.of() ) ).isEmpty();
    }
}
