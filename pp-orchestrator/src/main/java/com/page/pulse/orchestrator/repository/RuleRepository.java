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
    /**
     * Find a rule by its name.
     *
     * @param name the name of the rule
     * @return an optional containing the rule if found
     */
    Optional<Rule> findByName( String name );

}
