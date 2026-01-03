package com.page.pulse.syndication.api;

import com.page.pulse.domain.entity.Rule;
import com.page.pulse.syndication.mapper.RuleApiMapper;
import com.page.pulse.syndication.model.PagedRuleApiDto;
import com.page.pulse.syndication.model.RuleApiDto;
import com.page.pulse.syndication.service.RuleApiService;
import com.page.pulse.syndication.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the RulesApi interface.
 *
 * @author lewisjones
 */
@RestController
@RequiredArgsConstructor
public class RulesApiImpl implements RulesApi
{

    private final RuleApiService ruleApiService;
    private final RuleApiMapper ruleApiMapper = RuleApiMapper.INSTANCE;

    /**
     * Retrieves all rules with pagination and sorting.
     *
     * @param page the page number (0-indexed)
     * @param size the page size
     * @param sort the sorting criteria
     * @return a ResponseEntity containing the paged rules
     */
    @Override
    public ResponseEntity<PagedRuleApiDto> getRules( final Integer page, final Integer size, final List<String> sort )
    {
        final Pageable pageable = PaginationUtil.createPageable( page, size, sort );
        final Page<Rule> rulePage = ruleApiService.findAll( pageable );
        final PagedRuleApiDto pagedRuleApiDto = ruleApiMapper.toPagedApiDto( rulePage );
        return ResponseEntity.ok( pagedRuleApiDto );
    }

    /**
     * Retrieves a rule by its ID.
     *
     * @param id the rule ID
     * @return a ResponseEntity containing the rule if found, or 404 if not found
     */
    @Override
    public ResponseEntity<RuleApiDto> getRuleById( final Integer id )
    {
        final Optional<Rule> rule = ruleApiService.findById( id );
        return rule.map( r -> ResponseEntity.ok( ruleApiMapper.toApiDto( r ) ) )
            .orElse( ResponseEntity.notFound().build() );
    }
}
