package com.page.pulse.orchestrator.startup;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.orchestrator.repository.RuleRepository;
import com.page.pulse.orchestrator.rule.DocumentRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Startup task that discovers and persists DocumentRule implementations into the Rules table.
 * This component runs after the application context is fully initialized and ready.
 *
 * @author lewisjones
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RuleRegistrationStartupTask implements ApplicationListener<ApplicationReadyEvent>
{

    private final List<DocumentRule> documentRules;
    private final RuleRepository ruleRepository;

    /**
     * Handles the ApplicationReadyEvent to register all DocumentRule implementations.
     *
     * @param event the ApplicationReadyEvent
     */
    @Override
    @Transactional
    public void onApplicationEvent( final ApplicationReadyEvent event )
    {
        log.info( "Starting rule registration process..." );

        int registeredCount = 0;
        int updatedCount = 0;

        for ( final DocumentRule documentRule : documentRules )
        {
            final String ruleName = documentRule.name();
            final String ruleDescription = documentRule.description();

            log.debug( "Processing rule: {}", ruleName );

            final Optional<Rule> existingRule = ruleRepository.findByName( ruleName );

            if ( existingRule.isPresent() )
            {
                final Rule rule = existingRule.get();
                if ( !ruleDescription.equals( rule.getDescription() ) )
                {
                    rule.setDescription( ruleDescription );
                    ruleRepository.save( rule );
                    updatedCount++;
                    log.info( "Updated rule description: {}", ruleName );
                }
                else
                {
                    log.debug( "Rule already exists with same description: {}", ruleName );
                }
            }
            else
            {
                final Rule newRule = new Rule( ruleName, ruleDescription );
                ruleRepository.save( newRule );
                registeredCount++;
                log.info( "Registered new rule: {}", ruleName );
            }
        }

        log.info( "Rule registration completed. Registered: {}, Updated: {}, Total rules found: {}", registeredCount,
            updatedCount, documentRules.size() );
    }
}
