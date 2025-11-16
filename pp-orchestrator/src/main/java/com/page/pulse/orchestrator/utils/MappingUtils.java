package com.page.pulse.orchestrator.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

/**
 * Utility helpers used by mappers to safely convert common values from JsonNode.
 *
 * @author lewisjones
 */
public final class MappingUtils
{

    /**
     * Private constructor to prevent instantiation.
     */
    private MappingUtils()
    {
    }

    /**
     * Parse a date string into a LocalDateTime. Supports ISO-8601 formats with or without offsets.
     *
     * @param value the date string to parse
     * @return LocalDateTime or null if parsing fails
     */
    public static LocalDateTime parseDate( final String value )
    {
        if ( value == null || value.isBlank() )
        {
            return null;
        }

        try
        {
            // First try OffsetDateTime to support trailing Z and offsets
            final OffsetDateTime odt = OffsetDateTime.parse( value );
            return odt.atZoneSameInstant( ZoneId.systemDefault() ).toLocalDateTime();
        }
        catch ( final DateTimeParseException ignored )
        {
            try
            {
                // Fallback to LocalDateTime parse (no offset)
                return LocalDateTime.parse( value );
            }
            catch ( final DateTimeParseException ignored2 )
            {
                // Give one more try for LocalDate or Instant
                try
                {
                    final Instant inst = Instant.parse( value );
                    return LocalDateTime.ofInstant( inst, ZoneId.systemDefault() );
                }
                catch ( final DateTimeParseException ignored3 )
                {
                    return null;
                }
            }
        }
    }
}

