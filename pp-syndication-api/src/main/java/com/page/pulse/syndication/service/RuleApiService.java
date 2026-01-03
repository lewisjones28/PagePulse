package com.page.pulse.syndication.service;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.syndication.repository.RuleApiRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing Rule entities.
 *
 * @author lewisjones
 */
@Service
@AllArgsConstructor
public class RuleApiService
{

    private final RuleApiRepository ruleApiRepository;

    /**
     * Retrieves all rules with pagination.
     *
     * @param pageable the pagination information
     * @return a page of rules
     */
    public Page<Rule> findAll( final Pageable pageable )
    {
        return ruleApiRepository.findAll( pageable );
    }

    /**
     * Retrieves a rule by its ID.
     *
     * @param id the rule ID
     * @return an optional containing the rule if found
     */
    public Optional<Rule> findById( final Integer id )
    {
        return ruleApiRepository.findById( id );
    }
}
