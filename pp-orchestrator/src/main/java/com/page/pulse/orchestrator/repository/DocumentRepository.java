package com.page.pulse.orchestrator.repository;

import com.page.pulse.domain.entity.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for Document entities.
 *
 * @author lewisjones
 */
public interface DocumentRepository extends JpaRepository<Document, Long>
{
    Optional<Document> findByExternalId( String externalId );
}
