package com.page.pulse.syndication.repository;

import com.page.pulse.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Document} entities.
 *
 * @author lewisjones
 */
@Repository
public interface DocumentApiRepository extends JpaRepository<Document, Long>
{
}
