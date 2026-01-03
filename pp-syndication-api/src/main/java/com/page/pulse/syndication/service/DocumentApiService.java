package com.page.pulse.syndication.service;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.domain.entity.DocumentRuleViolation;
import com.page.pulse.syndication.repository.DocumentApiRepository;
import com.page.pulse.syndication.repository.DocumentRuleViolationApiRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link Document} entities.
 *
 * @author lewisjones
 */
@Service
@AllArgsConstructor
@Transactional( readOnly = true )
public class DocumentApiService
{

    private final DocumentApiRepository documentApiRepository;
    private final DocumentRuleViolationApiRepository violationRepository;

    /**
     * Retrieves a paginated list of Document entities.
     *
     * @param pageable pagination information
     * @return paginated list of Document entities
     */
    public Page<Document> getDocuments( final Pageable pageable )
    {
        return documentApiRepository.findAll( pageable );
    }

    /**
     * Retrieves rule violations for a list of documents.
     *
     * @param documents the documents to get violations for
     * @return map of document to list of rule violations
     */
    public Map<Document, List<DocumentRuleViolation>> getViolationsForDocuments( final List<Document> documents )
    {
        if ( documents == null || documents.isEmpty() )
        {
            return Map.of();
        }

        final List<DocumentRuleViolation> allViolations = violationRepository.findByDocumentIn( documents );
        return allViolations.stream()
            .collect( Collectors.groupingBy( DocumentRuleViolation::getDocument ) );
    }
}
