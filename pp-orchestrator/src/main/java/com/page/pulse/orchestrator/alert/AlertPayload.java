package com.page.pulse.orchestrator.alert;

import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;

/**
 * Payload describing rule evaluation details for alerts.
 *
 * @author lewisjones
 */
public record AlertPayload( DocumentDto document, RuleEvaluation evaluation )
{
    /**
     * Creates an AlertPayload instance with the provided document and rule evaluation.
     *
     * @param document   the document related to the alert
     * @param evaluation the rule evaluation details
     * @return a new AlertPayload instance
     */
    public static AlertPayload of( final DocumentDto document, final RuleEvaluation evaluation )
    {
        return new AlertPayload( document, evaluation );
    }
}

