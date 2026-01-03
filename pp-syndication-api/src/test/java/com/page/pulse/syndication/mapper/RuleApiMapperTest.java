package com.page.pulse.syndication.mapper;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.syndication.model.PagedRuleApiDto;
import com.page.pulse.syndication.model.RuleApiDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link RuleApiMapper}.
 *
 * @author lewisjones
 */
class RuleApiMapperTest
{

    private static final int PAGE_SIZE_10 = 10;
    private final RuleApiMapper ruleApiMapper = Mappers.getMapper( RuleApiMapper.class );

    @Test
    void shouldMapRuleToApiDto()
    {
        // given
        final Rule rule = new Rule( "test-rule", "Test rule description" );
        rule.setId( 1 );

        // when
        final RuleApiDto result = ruleApiMapper.toApiDto( rule );

        // then
        assertNotNull( result );
        assertEquals( 1, result.getId() );
        assertEquals( "test-rule", result.getName() );
        assertEquals( "Test rule description", result.getDescription() );
    }

    @Test
    void shouldMapPageToPagedApiDto()
    {
        // given
        final Rule rule1 = new Rule( "test-rule-1", "Test rule 1 description" );
        rule1.setId( 1 );
        final Rule rule2 = new Rule( "test-rule-2", "Test rule 2 description" );
        rule2.setId( 2 );

        final Page<Rule> page = new PageImpl<>( Arrays.asList( rule1, rule2 ), PageRequest.of( 0, PAGE_SIZE_10 ), 2 );

        // when
        final PagedRuleApiDto result = ruleApiMapper.toPagedApiDto( page );

        // then
        assertNotNull( result );
        assertNotNull( result.getContent() );
        assertEquals( 2, result.getContent().size() );

        assertEquals( 1, result.getContent().getFirst().getId() );
        assertEquals( "test-rule-1", result.getContent().get( 0 ).getName() );
        assertEquals( "Test rule 1 description", result.getContent().get( 0 ).getDescription() );

        assertEquals( 2, result.getContent().get( 1 ).getId() );
        assertEquals( "test-rule-2", result.getContent().get( 1 ).getName() );
        assertEquals( "Test rule 2 description", result.getContent().get( 1 ).getDescription() );

        assertNotNull( result.getPageInfo() );
        assertEquals( 0, result.getPageInfo().getPage() );
        assertEquals( 1, result.getPageInfo().getPages() );
        assertEquals( 2, result.getPageInfo().getElements() );
    }

    @Test
    void shouldHandleNullRule()
    {
        // when
        final RuleApiDto result = ruleApiMapper.toApiDto( null );

        // then
        assertNull( result );
    }
}
