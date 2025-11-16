package com.page.pulse.orchestrator.rule.impl;

import com.page.pulse.orchestrator.pojo.Document;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;
import com.page.pulse.orchestrator.rule.DocumentRule;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Rule that checks if a document is stale based on its last updated timestamp. A document is considered stale if it
 * has not been updated within a configured number of days.
 *
 * @author lewisjones
 */
@Component
public class StaleDocumentRule implements DocumentRule
{

    /**
     * Gets the name of the rule.
     *
     * @return the name of the rule
     */
    @Override
    public String name()
    {
        return "stale-document-rule";
    }

    /**
     * Evaluates the rule against the provided Document. A document is considered stale if it has not been updated in
     * the last X day(s).
     *
     * @param document the Document to evaluate
     * @return the result of the rule evaluation
     */
    @Override
    public RuleResult evaluate( final Document document )
    {
        final Duration age = Duration.between( document.updatedAt(), LocalDateTime.now() );
        final long interval = age.toDays();
        if ( interval > 1 )
        {
            return RuleResult.fail( "Document last updated " + interval + " days ago", document.externalId() );
        }

        return RuleResult.pass( document.externalId() );
    }

}
