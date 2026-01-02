package com.page.pulse.syndication.repository;

import com.page.pulse.domain.entity.Document;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Document entities.
 *
 * @author lewisjones
 */
@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, String> {
}
