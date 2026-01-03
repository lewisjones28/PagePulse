package com.page.pulse.orchestrator.rule;

import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;

/**
 * Interface for defining rules that can be evaluated against a Document.
 *
 * @author lewisjones
 */
public interface DocumentRule
{

    /**
     * Gets the name of the rule.
     *
     * @return the name of the rule
     */
    String name();

    /**
     * Gets the description of the rule.
     *
     * @return the description of the rule
     */
    String description();

    /**
     * Evaluates the rule against the provided Document.
     *
     * @param documentDto the Document to evaluate
     * @return the result of the rule evaluation
     */
    RuleResult evaluate( DocumentDto documentDto );
}
