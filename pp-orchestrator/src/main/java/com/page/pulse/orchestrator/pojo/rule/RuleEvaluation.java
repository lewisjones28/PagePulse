package com.page.pulse.orchestrator.pojo.rule;

/**
 * Represents the evaluation of a rule against a document.
 *
 * @param ruleName the name of the rule
 * @param result   the result of the rule evaluation
 * @author lewisjones
 */
public record RuleEvaluation( String ruleName, RuleResult result )
{
    /**
     * Determines if this evaluation has any alerts based on the result.
     *
     * @return true if there are alerts, false otherwise
     */
    public boolean hasAlerts()
    {
        return result != null && !result.passed();
    }
}
