package com.page.pulse.orchestrator.scheduled;

import com.page.pulse.confluence.client.page.params.ConfluencePageParams;
import com.page.pulse.domain.entity.document.Document;
import com.page.pulse.orchestrator.alert.AlertDispatcher;
import com.page.pulse.orchestrator.alert.AlertPayload;
import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.rule.engine.DocumentRuleEngine;
import com.page.pulse.orchestrator.service.ConfluenceApiService;
import com.page.pulse.orchestrator.service.DocumentRuleViolationService;
import com.page.pulse.orchestrator.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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
    private final DocumentService documentService;
    private final AlertDispatcher alertDispatcher;
    private final DocumentRuleViolationService violationService;

    /**
     * Constructs a DocumentScanTask with the provided ConfluenceApiService and DocumentRuleEngine.
     *
     * @param apiService the service to interact with Confluence API
     * @param ruleEngine the engine to evaluate document rules
     * @param documentService the service to persist documents
     * @param alertDispatcher the dispatcher to send alerts
     * @param violationService the service to manage rule violations
     */
    public DocumentScanTask( final ConfluenceApiService apiService, final DocumentRuleEngine ruleEngine,
                             final DocumentService documentService, final AlertDispatcher alertDispatcher,
                             final DocumentRuleViolationService violationService )
    {
        this.apiService = apiService;
        this.ruleEngine = ruleEngine;
        this.documentService = documentService;
        this.alertDispatcher = alertDispatcher;
        this.violationService = violationService;
    }

    /**
     * Runs a job to scan all documents, evaluating them against defined rules
     */
    @Scheduled( cron = "${document.cron-expression}" )
    public void documentScanTask()
    {
        log.info( "Starting documentScanTask" );

        final ConfluencePageParams params = ConfluencePageParams.empty()
            .status( Collections.singletonList( CURRENT_STATUS_PARAM ) );

        final List<DocumentDto> documentDtos = apiService.collectPages( params )
            .stream().toList();

        for ( final DocumentDto documentDto : documentDtos )
        {
            final Document document = documentService.saveOrUpdate( documentDto );
            final List<RuleEvaluation> evaluations = ruleEngine.evaluate( documentDto );

            for ( final RuleEvaluation evaluation : evaluations )
            {
                // Persist rule violation status
                violationService.saveOrUpdateViolation( document, evaluation );

                // Raise alert if there's a violation
                raiseAlert( documentDto, evaluation );
            }
        }

        log.info( "documentScanTask complete" );
    }

    /**
     * Raises alerts based on the provided rule evaluations.
     *
     * @param documentDto the DocumentDto associated with the evaluation
     * @param evaluation the RuleEvaluation to process
     */
    private void raiseAlert( final DocumentDto documentDto, final RuleEvaluation evaluation )
    {
        alertDispatcher.dispatch( AlertPayload.of( documentDto, evaluation ) );
    }

}
