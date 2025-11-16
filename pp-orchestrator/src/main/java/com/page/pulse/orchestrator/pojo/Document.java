package com.page.pulse.orchestrator.pojo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a document with various attributes.
 *
 * @param externalId      the external identifier of the document
 * @param externalOwnerId the external owner identifier of the document
 * @param title           the title of the document
 * @param status          the status of the document
 * @param tags            the list of tags associated with the document
 * @param createdAt       the creation timestamp of the document
 * @param updatedAt       the last updated timestamp of the document
 * @author lewisjones
 */
public record Document( String externalId, String externalOwnerId, String title, String status, List<String> tags,
                       LocalDateTime createdAt, LocalDateTime updatedAt )
{
}
