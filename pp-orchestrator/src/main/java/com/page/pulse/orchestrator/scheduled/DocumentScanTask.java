package com.page.pulse.orchestrator.scheduled;

import com.page.pulse.confluence.client.page.params.ConfluencePageParams;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.rule.engine.DocumentRuleEngine;
import com.page.pulse.orchestrator.service.ConfluenceApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.page.pulse.confluence.client.page.params.constants.ConfluencePageParamConstants.CURRENT_STATUS_PARAM;

/**
 * Scheduled task that scans documents in Confluence and evaluates them against defined rules.
 *
 * @author lewisjones
 */
@Component
public class DocumentScanTask
{

    private static final Logger log = LoggerFactory.getLogger( DocumentScanTask.class );
    private final ConfluenceApiService apiService;
    private final DocumentRuleEngine ruleEngine;

    /**
     * Constructs a DocumentScanTask with the provided ConfluenceApiService and DocumentRuleEngine.
     *
     * @param apiService the service to interact with Confluence API
     * @param ruleEngine the engine to evaluate document rules
     */
    public DocumentScanTask( final ConfluenceApiService apiService, final DocumentRuleEngine ruleEngine )
    {
        this.apiService = apiService;
        this.ruleEngine = ruleEngine;
    }

    /**
     * Runs a job to scan all documents, evaluating them against defined rules
     */
    @Scheduled( cron = "*/30 * * * * *" )
    public void documentScanTask()
    {
        log.info( "Starting documentScanTask" );

        final ConfluencePageParams params = ConfluencePageParams.empty()
            .status( Collections.singletonList( CURRENT_STATUS_PARAM ) );

        apiService.collectPages( params )
            .stream()
            .flatMap( doc -> ruleEngine.evaluate( doc ).stream() )
            .forEach( this::raiseAlert );

        log.info( "documentScanTask complete" );
    }

    /**
     * Raises alerts based on the provided rule evaluations.
     *
     * @param evaluation the RuleEvaluation to process
     */
    private void raiseAlert( final RuleEvaluation evaluation )
    {
        if ( evaluation.hasAlerts() )
        {
            log.warn( "⚠ [{}] Document {} FAILED: {}", evaluation.ruleName(), evaluation.result().documentId(),
                evaluation.result().message() );
        }
        else
        {
            log.info( "✅ [{}] Document {} PASSED", evaluation.ruleName(), evaluation.result().documentId() );
        }
    }

}
