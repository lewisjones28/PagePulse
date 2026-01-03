package com.page.pulse.syndication.repository;

import com.page.pulse.domain.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Rule entities in the syndication API.
 *
 * @author lewisjones
 */
@Repository
public interface RuleApiRepository extends JpaRepository<Rule, Integer>, PagingAndSortingRepository<Rule, Integer>
{
}
