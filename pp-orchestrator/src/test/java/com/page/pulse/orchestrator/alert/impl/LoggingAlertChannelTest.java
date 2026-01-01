package com.page.pulse.orchestrator.alert.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.page.pulse.orchestrator.alert.AlertPayload;
import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link LoggingAlertChannel}.
 *
 * @author lewisjones
 */
class LoggingAlertChannelTest
{
    private final LoggingAlertChannel channel = new LoggingAlertChannel();

    @Test
    void testSendWhenRulePasses()
    {
        final ListAppender<ILoggingEvent> appender = attachAppender();

        channel.send( buildPayload( true ) );

        assertThat( appender.list ).hasSize( 1 );
        final ILoggingEvent event = appender.list.getFirst();
        assertThat( event.getLevel() ).isEqualTo( Level.INFO );
        assertThat( event.getFormattedMessage() ).isEqualTo( "✅ [RuleA] Document doc-1 PASSED" );
    }

    @Test
    void testSendWhenRuleFails()
    {
        final ListAppender<ILoggingEvent> appender = attachAppender();

        channel.send( buildPayload( false ) );

        assertThat( appender.list ).hasSize( 1 );
        final ILoggingEvent event = appender.list.getFirst();
        assertThat( event.getLevel() ).isEqualTo( Level.WARN );
        assertThat( event.getFormattedMessage() ).isEqualTo( "⚠ [RuleA] Document doc-1 FAILED: Something went wrong" );
    }

    private ListAppender<ILoggingEvent> attachAppender()
    {
        final Logger logger = ( Logger ) LoggerFactory.getLogger( LoggingAlertChannel.class );
        final ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.detachAndStopAllAppenders();
        logger.addAppender( appender );
        return appender;
    }

    private AlertPayload buildPayload( final boolean passed )
    {
        final DocumentDto documentDto =
            new DocumentDto( "doc-1", "owner", "title", "status", List.of( "tag" ), LocalDateTime.now(),
                LocalDateTime.now() );
        final RuleResult result = passed ?
            RuleResult.pass( documentDto.externalId() ) :
            RuleResult.fail( "Something went wrong", documentDto.externalId() );
        final RuleEvaluation evaluation = new RuleEvaluation( "RuleA", result );
        return AlertPayload.of( documentDto, evaluation );
    }
}
