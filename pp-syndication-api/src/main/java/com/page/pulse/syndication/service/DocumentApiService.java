package com.page.pulse.syndication.service;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.syndication.repository.DocumentApiRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing {@link Document} entities.
 *
 * @author lewisjones
 */
@Service
@AllArgsConstructor
public class DocumentApiService
{

    private final DocumentApiRepository documentApiRepository;

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
}
