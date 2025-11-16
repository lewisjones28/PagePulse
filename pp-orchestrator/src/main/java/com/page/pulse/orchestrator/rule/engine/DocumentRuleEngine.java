package com.page.pulse.orchestrator.rule.engine;

import com.page.pulse.orchestrator.pojo.Document;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.rule.DocumentRule;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Engine that evaluates a set of DocumentRules against a given Document.
 *
 * @author lewisjones
 */
@Service
public class DocumentRuleEngine
{

    private final List<DocumentRule> rules;

    /**
     * Constructs a DocumentRuleEngine with the provided list of DocumentRules.
     *
     * @param rules the list of DocumentRules to be evaluated
     */
    public DocumentRuleEngine( final List<DocumentRule> rules )
    {
        this.rules = rules;
    }

    /**
     * Evaluates all registered rules against the provided Document.
     *
     * @param document the Document to evaluate
     * @return a list of RuleEvaluations representing the results of each rule evaluation
     */
    public List<RuleEvaluation> evaluate( final Document document )
    {
        return rules.stream().map( rule -> new RuleEvaluation( rule.name(), rule.evaluate( document ) ) ).toList();
    }
}
