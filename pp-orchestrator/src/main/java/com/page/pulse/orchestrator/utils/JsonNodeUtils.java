package com.page.pulse.orchestrator.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.StringUtils;

/**
 * Utility helpers for JsonNode.
 *
 * @author lewisjones
 */
public final class JsonNodeUtils
{
    /**
     * Private constructor to prevent instantiation.
     */
    private JsonNodeUtils()
    {
    }

    /**
     * Utility to safely extract text from a JsonNode field.
     *
     * @param node  the parent JsonNode
     * @param field the field name to extract
     * @return the text value, or null if missing
     */
    public static String getText( final JsonNode node, final String field )
    {
        if ( node == null )
        {
            return null;
        }

        final JsonNode textNode = node.path( field );
        if ( textNode.isNull() || !StringUtils.hasText( textNode.asText() ) )
        {
            return null;
        }

        return textNode.asText( null );
    }

}

