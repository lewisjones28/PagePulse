package com.page.pulse.orchestrator.repository;

import com.page.pulse.domain.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for Rule entities.
 *
 * @author lewisjones
 */
public interface RuleRepository extends JpaRepository<Rule, Integer>
{
    Optional<Rule> findByName( String name );
}
