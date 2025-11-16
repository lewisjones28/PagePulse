package com.page.pulse.orchestrator.mapper.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.page.pulse.orchestrator.mapper.BaseDocumentMapper;
import com.page.pulse.orchestrator.pojo.Document;
import com.page.pulse.orchestrator.utils.JsonNodeUtils;
import com.page.pulse.orchestrator.utils.MappingUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Confluence-specific mapper. Focused on the Confluence JSON shape for pages.
 *
 * @author lewisjones
 */
@Component
public class ConfluenceDocumentMapper extends BaseDocumentMapper<Document>
{
    /**
     * Maps a JsonNode representing a Confluence page to a Document object.
     *
     * @param node the JsonNode to map
     * @return the mapped Document object
     */
    @Override
    protected Document mapNode( final JsonNode node )
    {
        if ( node == null || node.isNull() )
        {
            return null;
        }

        final String externalId = JsonNodeUtils.getText( node, "id" );
        final String externalOwnerId = JsonNodeUtils.getText( node, "ownerId" );
        final String title = JsonNodeUtils.getText( node, "title" );
        final String status = JsonNodeUtils.getText( node, "status" );
        final String createdAt = JsonNodeUtils.getText( node, "createdAt" );
        final JsonNode versionNode = node.path( "version" );
        final String updatedAt = JsonNodeUtils.getText( versionNode, "createdAt" );
        final LocalDateTime createdAtDateTime = MappingUtils.parseDate( createdAt );
        final LocalDateTime updatedAtDateTime = MappingUtils.parseDate( updatedAt );
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
                    final String name = JsonNodeUtils.getText( labelNode, "name" );
                    if ( name != null )
                    {
                        tags.add( name );
                    }
                }
            }
        }

        return new Document( externalId, externalOwnerId, title, status, tags, createdAtDateTime, updatedAtDateTime );
    }
}
