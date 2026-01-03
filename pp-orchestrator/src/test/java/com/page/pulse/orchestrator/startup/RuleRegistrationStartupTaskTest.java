package com.page.pulse.orchestrator.startup;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.orchestrator.repository.RuleRepository;
import com.page.pulse.orchestrator.rule.DocumentRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit test for RuleRegistrationStartupTask.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class RuleRegistrationStartupTaskTest
{

    @Mock
    private RuleRepository ruleRepository;
    @Mock
    private DocumentRule documentRule1;
    @Mock
    private DocumentRule documentRule2;
    @Mock
    private ApplicationReadyEvent applicationReadyEvent;
    @InjectMocks
    private RuleRegistrationStartupTask ruleRegistrationStartupTask;

    @Test
    void shouldRegisterNewRules()
    {
        // given
        when( documentRule1.name() ).thenReturn( "test-rule-1" );
        when( documentRule1.description() ).thenReturn( "Test rule 1 description" );
        when( documentRule2.name() ).thenReturn( "test-rule-2" );
        when( documentRule2.description() ).thenReturn( "Test rule 2 description" );

        when( ruleRepository.findByName( anyString() ) ).thenReturn( Optional.empty() );

        ruleRegistrationStartupTask =
            new RuleRegistrationStartupTask( Arrays.asList( documentRule1, documentRule2 ), ruleRepository );

        // when
        ruleRegistrationStartupTask.onApplicationEvent( applicationReadyEvent );

        // then
        final ArgumentCaptor<Rule> ruleCaptor = ArgumentCaptor.forClass( Rule.class );
        verify( ruleRepository, times( 2 ) ).save( ruleCaptor.capture() );

        assertEquals( 2, ruleCaptor.getAllValues().size() );
        assertEquals( "test-rule-1", ruleCaptor.getAllValues().get( 0 ).getName() );
        assertEquals( "Test rule 1 description", ruleCaptor.getAllValues().get( 0 ).getDescription() );
        assertEquals( "test-rule-2", ruleCaptor.getAllValues().get( 1 ).getName() );
        assertEquals( "Test rule 2 description", ruleCaptor.getAllValues().get( 1 ).getDescription() );
    }

    @Test
    void shouldUpdateExistingRuleWithDifferentDescription()
    {
        // given
        when( documentRule1.name() ).thenReturn( "existing-rule" );
        when( documentRule1.description() ).thenReturn( "Updated description" );

        final Rule existingRule = new Rule( "existing-rule", "Old description" );
        when( ruleRepository.findByName( "existing-rule" ) ).thenReturn( Optional.of( existingRule ) );

        ruleRegistrationStartupTask = new RuleRegistrationStartupTask( List.of( documentRule1 ), ruleRepository );

        // when
        ruleRegistrationStartupTask.onApplicationEvent( applicationReadyEvent );

        // then
        final ArgumentCaptor<Rule> ruleCaptor = ArgumentCaptor.forClass( Rule.class );
        verify( ruleRepository ).save( ruleCaptor.capture() );

        assertEquals( "existing-rule", ruleCaptor.getValue().getName() );
        assertEquals( "Updated description", ruleCaptor.getValue().getDescription() );
    }

    @Test
    void shouldNotUpdateExistingRuleWithSameDescription()
    {
        // given
        when( documentRule1.name() ).thenReturn( "existing-rule" );
        when( documentRule1.description() ).thenReturn( "Same description" );

        final Rule existingRule = new Rule( "existing-rule", "Same description" );
        when( ruleRepository.findByName( "existing-rule" ) ).thenReturn( Optional.of( existingRule ) );

        ruleRegistrationStartupTask = new RuleRegistrationStartupTask( Arrays.asList( documentRule1 ), ruleRepository );

        // when
        ruleRegistrationStartupTask.onApplicationEvent( applicationReadyEvent );

        // then
        verify( ruleRepository, never() ).save( any( Rule.class ) );
    }
}
