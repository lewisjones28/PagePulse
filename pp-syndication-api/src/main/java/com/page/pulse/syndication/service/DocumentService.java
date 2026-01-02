package com.page.pulse.syndication.service;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.syndication.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Document entities.
 *
 * @author lewisjones
 */
@Service
@AllArgsConstructor
public class DocumentService
{

    private final DocumentRepository documentRepository;

    /**
     * Retrieves a paginated list of Document entities.
     *
     * @param pageable pagination information
     * @return paginated list of Document entities
     */
    public Page<Document> getDocuments( final Pageable pageable )
    {
        return documentRepository.findAll( pageable );
    }
}

