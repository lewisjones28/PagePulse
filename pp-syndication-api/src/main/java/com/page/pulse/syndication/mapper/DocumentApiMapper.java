package com.page.pulse.syndication.mapper;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.domain.entity.DocumentRuleViolation;
import com.page.pulse.syndication.model.DocumentApiDto;
import com.page.pulse.syndication.model.RuleViolationApiDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Maps a Document entity to a DocumentApiDto.
     *
     * @param document the Document entity to map
     * @return the corresponding DocumentApiDto
     */
    @Mapping( target = "id", source = "id" )
    @Mapping( target = "ruleViolations", ignore = true )
    // Will be set manually
    DocumentApiDto toDto( Document document );

    /**
     * Maps a Document entity to a DocumentApiDto with rule violations.
     *
     * @param document   the Document entity to map
     * @param violations the list of rule violations for the document
     * @return the corresponding DocumentApiDto
     */
    default DocumentApiDto toDtoWithViolations( final Document document, final List<DocumentRuleViolation> violations )
    {
        final DocumentApiDto dto = toDto( document );
        if ( violations != null && !violations.isEmpty() )
        {
            final List<RuleViolationApiDto> violationDtos =
                violations.stream().map( this::mapViolation ).collect( Collectors.toList() );
            dto.setRuleViolations( violationDtos );
        }
        else
        {
            dto.setRuleViolations( Collections.emptyList() );
        }
        return dto;
    }

    /**
     * Maps a DocumentRuleViolation entity to a RuleViolationApiDto.
     *
     * @param violation the violation entity
     * @return the violation DTO
     */
    default RuleViolationApiDto mapViolation( DocumentRuleViolation violation )
    {
        final RuleViolationApiDto dto = new RuleViolationApiDto();
        dto.setId( violation.getId() );
        dto.setViolating( violation.getViolating() );
        dto.setViolationDetails( violation.getViolationDetails() );
        dto.setCreatedAt( map( violation.getCreatedOn() ) );
        dto.setUpdatedAt( map( violation.getUpdatedOn() ) );

        // Map the rule using RuleApiMapper
        if ( violation.getRule() != null )
        {
            dto.setRule( RuleApiMapper.INSTANCE.toApiDto( violation.getRule() ) );
        }

        return dto;
    }

    /**
     * Maps a LocalDateTime to an OffsetDateTime with UTC offset.
     *
     * @param value the LocalDateTime to map
     * @return the corresponding OffsetDateTime
     */
    default OffsetDateTime map( final LocalDateTime value )
    {
        return value == null ? null : value.atOffset( ZoneOffset.UTC );
    }
}
