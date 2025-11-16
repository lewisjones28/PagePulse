package com.page.pulse.orchestrator.mapper.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.page.pulse.confluence.client.page.params.constants.ConfluencePageResponseConstants;
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

        final String externalId = JsonNodeUtils.getText( node, ConfluencePageResponseConstants.PAGE_ID_ATTRIBUTE );
        final String externalOwnerId =
            JsonNodeUtils.getText( node, ConfluencePageResponseConstants.PAGE_OWNER_ID_ATTRIBUTE );
        final String title = JsonNodeUtils.getText( node, ConfluencePageResponseConstants.PAGE_TITLE_ATTRIBUTE );
        final String status = JsonNodeUtils.getText( node, ConfluencePageResponseConstants.PAGE_STATUS_ATTRIBUTE );
        final String createdAt =
            JsonNodeUtils.getText( node, ConfluencePageResponseConstants.PAGE_CREATED_AT_ATTRIBUTE );
        final JsonNode versionNode = node.path( ConfluencePageResponseConstants.VERSION_ATTRIBUTE );
        final String updatedAt =
            JsonNodeUtils.getText( versionNode, ConfluencePageResponseConstants.VERSION_CREATED_AT_ATTRIBUTE );
        final LocalDateTime createdAtDateTime = MappingUtils.parseDate( createdAt );
        final LocalDateTime updatedAtDateTime = MappingUtils.parseDate( updatedAt );
        final List<String> tags = new ArrayList<>();
        final JsonNode labelsNode = node.path( ConfluencePageResponseConstants.PAGE_LABELS_ATTRIBUTE );
        if ( !labelsNode.isMissingNode() && !labelsNode.isNull() &&
            !labelsNode.get( ConfluencePageResponseConstants.LABELS_RESULTS_ATTRIBUTE ).isEmpty() )
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
                    final String name =
                        JsonNodeUtils.getText( labelNode, ConfluencePageResponseConstants.LABEL_NAME_ATTRIBUTE );
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
