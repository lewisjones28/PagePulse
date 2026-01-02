package com.page.pulse.syndication.mapper;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.syndication.model.DocumentApiDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Mapper interface for converting Document entities to DocumentApiDto objects.
 *
 * @author lewisjones
 */
@Mapper
public interface DocumentApiMapper
{

    /**
     * Singleton instance of the DocumentMapper.
     */
    DocumentApiMapper INSTANCE = Mappers.getMapper( DocumentApiMapper.class );

    @Mapping( target = "id", source = "id" )
    DocumentApiDto toDto( Document document );

    /**
     * Maps a LocalDateTime to an OffsetDateTime with UTC offset.
     *
     * @param value the LocalDateTime to map
     * @return the corresponding OffsetDateTime
     */
    default OffsetDateTime map( final LocalDateTime value )
    {
        return value.atOffset( ZoneOffset.UTC );
    }
}
