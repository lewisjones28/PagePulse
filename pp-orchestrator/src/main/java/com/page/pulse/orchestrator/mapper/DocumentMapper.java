package com.page.pulse.orchestrator.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.orchestrator.pojo.Document;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MapStruct mapper for converting JsonNode objects to Document POJOs.
 *
 * @author lewisjones
 */
@Mapper( componentModel = "spring" )
public interface DocumentMapper
{

    /**
     * Convert a JsonNode representing a collection of pages into a List of Document objects.
     *
     * @param pages the JsonNode list containing page data
     * @return List of Document objects.
     */
    default List<Document> toDocumentList( final List<JsonNode> pages )
    {
        if ( pages == null || pages.isEmpty() )
        {
            return Collections.emptyList();
        }

        final List<Document> results = new ArrayList<>();
        for ( final JsonNode item : pages )
        {
            final Document d = toDocument( item );
            if ( d != null )
            {
                results.add( d );
            }
        }

        return results;
    }

    /**
     * Convert a JsonNode representing a single page into a Document object.
     *
     * @param node the JsonNode containing page data
     * @return Document object
     */
    default Document toDocument( final JsonNode node )
    {
        if ( node == null || node.isNull() )
        {
            return null;
        }

        final String id = node.path( "id" ).asText( null );
        final String title = node.path( "title" ).asText( null );
        final String status = node.path( "status" ).asText( null );
        final List<String> tags = new ArrayList<>();
        final JsonNode labelsNode = node.path( "labels" );
        if ( !labelsNode.isMissingNode() && !labelsNode.isNull() && !labelsNode.get( "results" ).isEmpty() )
        {
            for ( final JsonNode label : labelsNode )
            {
                tags.add( label.asText( null ) );
            }
        }

        return new Document( id, title, status, tags );
    }

}
