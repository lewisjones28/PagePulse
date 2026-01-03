package com.page.pulse.orchestrator.service;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.domain.entity.DocumentRuleViolation;
import com.page.pulse.domain.entity.Rule;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.repository.DocumentRuleViolationRepository;
import com.page.pulse.orchestrator.repository.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service for managing document rule violations.
 *
 * @author lewisjones
 */
@Service
public class DocumentRuleViolationService
{
    private static final Logger log = LoggerFactory.getLogger( DocumentRuleViolationService.class );
    private final DocumentRuleViolationRepository violationRepository;
    private final RuleRepository ruleRepository;

    /**
     * Constructs a DocumentRuleViolationService with the provided repositories.
     *
     * @param violationRepository the violation repository
     * @param ruleRepository      the rule repository
     */
    public DocumentRuleViolationService( final DocumentRuleViolationRepository violationRepository,
        final RuleRepository ruleRepository )
    {
        this.violationRepository = violationRepository;
        this.ruleRepository = ruleRepository;
    }

    /**
     * Save or update a document rule violation based on rule evaluation.
     *
     * @param document   the document being evaluated
     * @param evaluation the rule evaluation result
     * @return the saved or updated violation record
     */
    @Transactional
    public DocumentRuleViolation saveOrUpdateViolation( final Document document, final RuleEvaluation evaluation )
    {
        final Optional<Rule> ruleOpt = ruleRepository.findByName( evaluation.ruleName() );
        if ( ruleOpt.isEmpty() )
        {
            log.warn( "Rule not found: {}", evaluation.ruleName() );
            return null;
        }

        final Rule rule = ruleOpt.get();
        final boolean isViolating = evaluation.hasAlerts();
        final String violationDetails = evaluation.result().message();
        return violationRepository.findByDocumentAndRule( document, rule )
            .map( existing -> updateExistingViolation( existing, isViolating, violationDetails ) )
            .orElseGet( () -> createNewViolation( document, rule, isViolating, violationDetails ) );
    }

    /**
     * Update an existing violation record.
     *
     * @param existing         the existing violation record
     * @param isViolating      whether the document is currently violating the rule
     * @param violationDetails details about the violation
     * @return the updated violation record
     */
    private DocumentRuleViolation updateExistingViolation( final DocumentRuleViolation existing,
        final boolean isViolating, final String violationDetails )
    {
        existing.setViolating( isViolating );
        existing.setViolationDetails( violationDetails );
        return violationRepository.save( existing );
    }

    /**
     * Create a new violation record.
     *
     * @param document         the document
     * @param rule             the rule
     * @param isViolating      whether the document is violating the rule
     * @param violationDetails details about the violation
     * @return the new violation record
     */
    private DocumentRuleViolation createNewViolation( final Document document, final Rule rule,
        final boolean isViolating, final String violationDetails )
    {
        final DocumentRuleViolation newViolation =
            new DocumentRuleViolation( document, rule, isViolating, violationDetails );
        return violationRepository.save( newViolation );
    }

    /**
     * Check if a document violates a specific rule.
     *
     * @param document the document to check
     * @param ruleName the name of the rule to check
     * @return true if the document violates the rule, false otherwise
     */
    public boolean isDocumentViolatingRule( final Document document, final String ruleName )
    {
        return ruleRepository.findByName( ruleName )
            .map( rule -> violationRepository.isDocumentViolatingRule( document, rule ) )
            .orElse( false );
    }
}
