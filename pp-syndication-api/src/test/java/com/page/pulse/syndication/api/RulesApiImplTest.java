package com.page.pulse.syndication.api;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.syndication.repository.RuleApiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for {@link RulesApiImpl}.
 *
 * @author lewisjones
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles( "test" )
class RulesApiImplTest
{

    private static final int NON_EXISTENT_RULE_ID = 999;
    private static final int TOTAL_RULES_FOR_PAGINATION = 15;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RuleApiRepository ruleApiRepository;
    @BeforeEach
    void setUp()
    {
        ruleApiRepository.deleteAll();
    }

    @Test
    void shouldGetAllRules() throws Exception
    {
        // given
        final Rule rule1 = new Rule( "test-rule-1", "Test rule 1 description" );
        rule1.setCreatedBy( "testUser" );
        rule1.setCreatedOn( LocalDateTime.now() );

        final Rule rule2 = new Rule( "test-rule-2", "Test rule 2 description" );
        rule2.setCreatedBy( "testUser" );
        rule2.setCreatedOn( LocalDateTime.now() );

        ruleApiRepository.save( rule1 );
        ruleApiRepository.save( rule2 );

        // when & then
        mockMvc.perform(
                get( "/rules" ).param( "page", "0" ).param( "size", "10" ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isOk() )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.content" ).isArray() )
            .andExpect( jsonPath( "$.content.length()" ).value( 2 ) )
            .andExpect( jsonPath( "$.content[0].name" ).value( "test-rule-1" ) )
            .andExpect( jsonPath( "$.content[0].description" ).value( "Test rule 1 description" ) )
            .andExpect( jsonPath( "$.content[1].name" ).value( "test-rule-2" ) )
            .andExpect( jsonPath( "$.content[1].description" ).value( "Test rule 2 description" ) )
            .andExpect( jsonPath( "$.pageInfo.page" ).value( 0 ) )
            .andExpect( jsonPath( "$.pageInfo.elements" ).value( 2 ) );
    }

    @Test
    void shouldGetRuleById() throws Exception
    {
        // given
        final Rule rule = new Rule( "test-rule", "Test rule description" );
        rule.setCreatedBy( "testUser" );
        rule.setCreatedOn( LocalDateTime.now() );
        final Rule savedRule = ruleApiRepository.save( rule );

        // when & then
        mockMvc.perform( get( "/rules/{id}", savedRule.getId() ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isOk() )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.id" ).value( savedRule.getId() ) )
            .andExpect( jsonPath( "$.name" ).value( "test-rule" ) )
            .andExpect( jsonPath( "$.description" ).value( "Test rule description" ) );
    }

    @Test
    void shouldReturn404WhenRuleNotFound() throws Exception
    {
        // when & then
        mockMvc.perform( get( "/rules/{id}", NON_EXISTENT_RULE_ID ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isNotFound() );
    }

    @Test
    void shouldGetRulesWithPagination() throws Exception
    {
        // given
        for ( int i = 1; i <= TOTAL_RULES_FOR_PAGINATION; i++ )
        {
            final Rule rule = new Rule( "rule-" + i, "Description " + i );
            rule.setCreatedBy( "testUser" );
            rule.setCreatedOn( LocalDateTime.now() );
            ruleApiRepository.save( rule );
        }

        // when & then - First page
        mockMvc.perform(
                get( "/rules" ).param( "page", "0" ).param( "size", "5" ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.content.length()" ).value( 5 ) )
            .andExpect( jsonPath( "$.pageInfo.page" ).value( 0 ) )
            .andExpect( jsonPath( "$.pageInfo.pages" ).value( 3 ) )
            .andExpect( jsonPath( "$.pageInfo.elements" ).value( TOTAL_RULES_FOR_PAGINATION ) );

        // when & then - Second page
        mockMvc.perform(
                get( "/rules" ).param( "page", "1" ).param( "size", "5" ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.content.length()" ).value( 5 ) )
            .andExpect( jsonPath( "$.pageInfo.page" ).value( 1 ) );
    }

    @Test
    void shouldGetRulesWithSorting() throws Exception
    {
        // given
        final Rule rule1 = new Rule( "zebra-rule", "Zebra description" );
        rule1.setCreatedBy( "testUser" );
        rule1.setCreatedOn( LocalDateTime.now() );

        final Rule rule2 = new Rule( "alpha-rule", "Alpha description" );
        rule2.setCreatedBy( "testUser" );
        rule2.setCreatedOn( LocalDateTime.now() );

        ruleApiRepository.save( rule1 );
        ruleApiRepository.save( rule2 );

        // when & then - Sort by name ascending
        mockMvc.perform( get( "/rules" ).param( "sort", "name,asc" ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.content[0].name" ).value( "alpha-rule" ) )
            .andExpect( jsonPath( "$.content[1].name" ).value( "zebra-rule" ) );

        // when & then - Sort by name descending
        mockMvc.perform( get( "/rules" ).param( "sort", "name,desc" ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.content[0].name" ).value( "zebra-rule" ) )
            .andExpect( jsonPath( "$.content[1].name" ).value( "alpha-rule" ) );
    }
}
