package com.page.pulse.syndication.mapper;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.syndication.model.PagedRuleApiDto;
import com.page.pulse.syndication.model.RuleApiDto;
import com.page.pulse.syndication.util.PaginationUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

/**
 * Mapper for converting between Rule entities and RuleApiDto objects.
 *
 * @author lewisjones
 */
@Mapper
public interface RuleApiMapper
{

    /**
     * Singleton instance of the RuleApiMapper.
     */
    RuleApiMapper INSTANCE = Mappers.getMapper( RuleApiMapper.class );

    /**
     * Maps a Rule entity to a RuleApiDto.
     *
     * @param rule the Rule entity
     * @return the RuleApiDto
     */
    RuleApiDto toApiDto( Rule rule );

    /**
     * Maps a Page of Rule entities to a PagedRuleApiDto.
     *
     * @param page the Page of Rule entities
     * @return the PagedRuleApiDto
     */
    default PagedRuleApiDto toPagedApiDto( final Page<Rule> page )
    {
        final PagedRuleApiDto pagedDto = new PagedRuleApiDto();
        pagedDto.setContent( page.getContent().stream().map( this::toApiDto ).toList() );
        pagedDto.setPageInfo( PaginationUtil.toPageInfo( page ) );
        return pagedDto;
    }
}
