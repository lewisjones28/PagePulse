package com.page.pulse.orchestrator.alert.impl;

import com.page.pulse.orchestrator.alert.AlertChannel;
import com.page.pulse.orchestrator.alert.AlertPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Default alert channel emitting pass/fail information to the application log.
 */
@Component
public class LoggingAlertChannel implements AlertChannel
{
    private static final Logger log = LoggerFactory.getLogger( LoggingAlertChannel.class );

    @Override
    public void send( final AlertPayload payload )
    {
        final String ruleName = payload.evaluation().ruleName();
        final String documentId = payload.evaluation().result().documentId();
        if ( payload.evaluation().hasAlerts() )
        {
            log.warn( "⚠ [{}] Document {} FAILED: {}", ruleName, documentId, payload.evaluation().result().message() );
        }
        else
        {
            log.info( "✅ [{}] Document {} PASSED", ruleName, documentId );
        }
    }
}
