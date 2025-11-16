package com.page.pulse.orchestrator.pojo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Immutable POJO representing a Document (record form).
 *
 * @author lewisjones
 */
public record Document( String externalId, String title, String status, List<String> tags, LocalDateTime createdAt )
{
}
