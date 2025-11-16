package com.page.pulse.orchestrator.pojo;

import java.util.List;

/**
 * Immutable POJO representing a Document (record form).
 *
 * @author lewisjones
 */
public record Document( String id, String title, String status, List<String> tags )
{
}
