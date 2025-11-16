package com.page.pulse.orchestrator.rule;

import com.page.pulse.orchestrator.pojo.Document;
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
     * Evaluates the rule against the provided Document.
     *
     * @param document the Document to evaluate
     * @return the result of the rule evaluation
     */
    RuleResult evaluate( Document document );
}
