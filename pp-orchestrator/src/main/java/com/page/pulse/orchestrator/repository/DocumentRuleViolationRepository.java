package com.page.pulse.orchestrator.repository;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.domain.entity.DocumentRuleViolation;
import com.page.pulse.domain.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link DocumentRuleViolation} entities.
 *
 * @author lewisjones
 */
@Repository
public interface DocumentRuleViolationRepository extends JpaRepository<DocumentRuleViolation, Long>
{
    /**
     * Find a document-rule violation by document and rule.
     *
     * @param document the document to search for
     * @param rule     the rule to search for
     * @return an optional containing the violation if found
     */
    Optional<DocumentRuleViolation> findByDocumentAndRule( Document document, Rule rule );

    /**
     * Check if a document violates a specific rule.
     *
     * @param document the document to check
     * @param rule     the rule to check against
     * @return true if the document violates the rule, false otherwise
     */
    @Query( "SELECT CASE WHEN COUNT(drv) > 0 THEN true ELSE false END " + "FROM DocumentRuleViolation drv " +
        "WHERE drv.document = :document AND drv.rule = :rule AND drv.violating = true" )
    boolean isDocumentViolatingRule( @Param( "document" ) Document document, @Param( "rule" ) Rule rule );

}
