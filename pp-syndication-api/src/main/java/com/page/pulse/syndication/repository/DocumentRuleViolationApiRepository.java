package com.page.pulse.syndication.repository;

import com.page.pulse.domain.entity.document.Document;
import com.page.pulse.domain.entity.document.DocumentRuleViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for DocumentRuleViolation entities in the syndication API.
 *
 * @author lewisjones
 */
@Repository
public interface DocumentRuleViolationApiRepository extends JpaRepository<DocumentRuleViolation, Long>
{

    /**
     * Find all violations for a list of documents with proper eager fetching.
     *
     * @param documents the list of documents to search for
     * @return a list of violations for the documents
     */
    @Query( "SELECT drv FROM DocumentRuleViolation drv " +
           "JOIN FETCH drv.document " +
           "JOIN FETCH drv.rule " + "WHERE drv.document IN :documents" )
    List<DocumentRuleViolation> findByDocumentIn( @Param( "documents" ) List<Document> documents );

}
