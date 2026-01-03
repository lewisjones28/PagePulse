package com.page.pulse.syndication.service;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.syndication.repository.RuleApiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for RuleService.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class RuleApiServiceTest
{

    @Mock
    private RuleApiRepository ruleApiRepository;
    @InjectMocks
    private RuleApiService ruleApiService;

    @Test
    void shouldFindAllRulesWithPagination()
    {
        // Given
        final Rule rule1 = new Rule( "test-rule-1", "Test rule 1 description" );
        final Rule rule2 = new Rule( "test-rule-2", "Test rule 2 description" );
        final Pageable pageable = PageRequest.of( 0, 10 );
        final Page<Rule> rulePage = new PageImpl<>( Arrays.asList( rule1, rule2 ), pageable, 2 );

        when( ruleApiRepository.findAll( pageable ) ).thenReturn( rulePage );

        // When
        final Page<Rule> result = ruleApiService.findAll( pageable );

        // Then
        assertNotNull( result );
        assertEquals( 2, result.getContent().size() );
        assertEquals( "test-rule-1", result.getContent().get( 0 ).getName() );
        assertEquals( "test-rule-2", result.getContent().get( 1 ).getName() );
        verify( ruleApiRepository ).findAll( pageable );
    }

    @Test
    void shouldFindRuleById()
    {
        // Given
        final Integer ruleId = 1;
        final Rule rule = new Rule( "test-rule", "Test rule description" );
        rule.setId( ruleId );

        when( ruleApiRepository.findById( ruleId ) ).thenReturn( Optional.of( rule ) );

        // When
        final Optional<Rule> result = ruleApiService.findById( ruleId );

        // Then
        assertTrue( result.isPresent() );
        assertEquals( "test-rule", result.get().getName() );
        assertEquals( "Test rule description", result.get().getDescription() );
        verify( ruleApiRepository ).findById( ruleId );
    }

    @Test
    void shouldReturnEmptyWhenRuleNotFound()
    {
        // Given
        final Integer ruleId = 999;
        when( ruleApiRepository.findById( ruleId ) ).thenReturn( Optional.empty() );

        // When
        final Optional<Rule> result = ruleApiService.findById( ruleId );

        // Then
        assertFalse( result.isPresent() );
        verify( ruleApiRepository ).findById( ruleId );
    }
}
