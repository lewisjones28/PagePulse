package com.page.pulse.orchestrator.scheduled;

import com.page.pulse.confluence.client.ConfluencePageParams;
import com.page.pulse.orchestrator.pojo.Document;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.rule.engine.DocumentRuleEngine;
import com.page.pulse.orchestrator.service.ConfluenceApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
    @Scheduled( cron = "* * * * * *" ) // Every 1 minute for demo purposes
    public void documentScanTask()
    {
        log.info( "Starting documentScanTask" );

        // Fetch all documents
        final List<Document> docs = apiService.collectPages( ConfluencePageParams.empty() );
        log.info( "Fetched {} documents", docs.size() );

        // Evaluate each document with all rules
        final List<RuleEvaluation> evaluations =
            docs.stream().flatMap( doc -> ruleEngine.evaluate( doc ).stream() ).toList();

        log.info( "Completed rule evaluation: {} evaluations generated", evaluations.size() );

        // Extract any actual alerts from evaluations
        evaluations.stream().filter( RuleEvaluation::hasAlerts )
            .forEach( eval ->
            {
                log.warn( "⚠ [{}] Document {} - {}", eval.ruleName(), eval.result().documentId(),
                    eval.result().message() );
            } );

        log.info( "documentScanTask complete" );
    }

}
