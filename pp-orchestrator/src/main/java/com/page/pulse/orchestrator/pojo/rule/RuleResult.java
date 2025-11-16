package com.page.pulse.orchestrator.pojo.rule;

/**
 * Represents the result of evaluating a rule.
 *
 * @param passed     indicates if the rule passed or failed
 * @param message    provides additional information about the result
 * @param documentId the ID of the document evaluated
 * @author lewisjones
 */
public record RuleResult( boolean passed, String message, String documentId )
{
    /**
     * Creates a RuleResult indicating a passing result.
     *
     * @param externalDocumentId the ID of the document evaluated
     * @return a RuleResult with passed set to true and message "OK"
     */
    public static RuleResult pass( final String externalDocumentId )
    {
        return new RuleResult( Boolean.TRUE, "OK", externalDocumentId );
    }

    /**
     * Creates a RuleResult indicating a failing result with a specified reason.
     *
     * @param reason             the reason for the failure
     * @param externalDocumentId the ID of the document evaluated
     * @return a RuleResult with passed set to false and the provided reason
     */
    public static RuleResult fail( final String reason, final String externalDocumentId )
    {
        return new RuleResult( Boolean.FALSE, reason, externalDocumentId );
    }
}
