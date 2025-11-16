package com.page.pulse.orchestrator.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic base mapper for converting JsonNode page responses into domain Documents.
 * Subclasses should implement {@link #mapNode(JsonNode)} to transform a single page node.
 *
 * @author lewisjones
 */
public abstract class BaseDocumentMapper<T>
{

    /**
     * Transform a JsonNode representing multiple pages into a list of document instances.
     *
     * @param pagesNode the JsonNode representing multiple pages
     * @return the list of mapped document instances
     */
    public List<T> toDocumentList( final JsonNode pagesNode )
    {
        if ( pagesNode == null || pagesNode.isNull() )
        {
            return Collections.emptyList();
        }

        // prefer `results` if present, otherwise iterate the node directly
        JsonNode items = pagesNode.path( "results" );
        if ( items.isMissingNode() || items.isNull() || !items.elements().hasNext() )
        {
            items = pagesNode;
        }

        // Convert the chosen iterable into a list and delegate to the List-based overload
        final List<JsonNode> pages = new ArrayList<>();
        for ( final JsonNode item : items )
        {
            pages.add( item );
        }

        if ( pages.isEmpty() )
        {
            return Collections.emptyList();
        }

        return toDocumentList( pages );
    }

    /**
     * Transform a pre-built list of JsonNode page details into a list of document instances.
     *
     * @param pages the list of JsonNode representing multiple pages
     * @return the list of mapped document instances
     */
    public List<T> toDocumentList( final List<JsonNode> pages )
    {
        if ( pages == null || pages.isEmpty() )
        {
            return Collections.emptyList();
        }
        final List<T> results = new ArrayList<>();
        for ( final JsonNode item : pages )
        {
            final T d = mapNode( item );
            if ( d != null )
            {
                results.add( d );
            }
        }
        return results;
    }

    /**
     * Transform a single page JsonNode into a document instance.
     *
     * @param node the JsonNode representing a single page
     * @return the mapped document instance
     */
    protected abstract T mapNode( final JsonNode node );

    /**
     * Utility to safely extract text from a JsonNode field.
     *
     * @param node  the parent JsonNode
     * @param field the field name to extract
     * @return the text value, or null if missing
     */
    protected String getText( final JsonNode node, final String field )
    {
        if ( node == null || node.isNull() )
        {
            return null;
        }
        return node.path( field ).asText( null );
    }
}
