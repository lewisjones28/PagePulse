package com.page.pulse.orchestrator.mapper.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.page.pulse.orchestrator.mapper.BaseDocumentMapper;
import com.page.pulse.orchestrator.pojo.Document;
import com.page.pulse.orchestrator.utils.MappingUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Confluence-specific mapper. Focused on the Confluence JSON shape for pages.
 */
@Component
public class ConfluenceDocumentMapper extends BaseDocumentMapper<Document>
{
    @Override
    protected Document mapNode( final JsonNode node )
    {
        if ( node == null || node.isNull() )
        {
            return null;
        }

        final String externalId = getText( node, "id" );
        final String title = getText( node, "title" );
        final String status = getText( node, "status" );
        final LocalDateTime createdAt = MappingUtils.parseDate( node.path( "createdAt" ).asText( null ) );
        final List<String> tags = new ArrayList<>();
        final JsonNode labelsNode = node.path( "labels" );
        if ( !labelsNode.isMissingNode() && !labelsNode.isNull() && !labelsNode.get( "results" ).isEmpty() )
        {
            for ( final JsonNode label : labelsNode )
            {
                if ( !label.isArray() )
                {
                    continue;
                }

                final ArrayNode results = ( ArrayNode ) label;
                for ( final JsonNode labelNode : results )
                {
                    final String name = labelNode.path( "name" ).asText( null );
                    if ( name != null )
                    {
                        tags.add( name );
                    }
                }
            }
        }

        return new Document( externalId, title, status, tags, createdAt );
    }
}
