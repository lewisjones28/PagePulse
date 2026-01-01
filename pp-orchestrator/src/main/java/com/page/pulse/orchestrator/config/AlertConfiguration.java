package com.page.pulse.orchestrator.config;

import com.page.pulse.orchestrator.alert.AlertChannel;
import com.page.pulse.orchestrator.alert.AlertDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wires the available alert channels into a dispatcher bean.
 *
 * @author lewisjones
 */
@Configuration
public class AlertConfiguration
{
    @Bean
    AlertDispatcher alertDispatcher( final List<AlertChannel> channels )
    {
        return new AlertDispatcher( channels );
    }
}

