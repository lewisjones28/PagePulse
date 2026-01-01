package com.page.pulse.orchestrator.alert;

import java.util.List;

/**
 * Broadcasts alert payloads to all configured channels.
 *
 * @author lewisjones
 */
public class AlertDispatcher
{
    private final List<AlertChannel> channels;

    /**
     * Constructs an AlertDispatcher with the specified alert channels.
     *
     * @param channels the list of alert channels to which alerts will be dispatched
     */
    public AlertDispatcher( final List<AlertChannel> channels )
    {
        this.channels = channels;
    }

    /**
     * Dispatches the given alert payload to all configured alert channels.
     *
     * @param payload the alert payload to be dispatched
     */
    public void dispatch( final AlertPayload payload )
    {
        channels.forEach( channel -> channel.send( payload ) );
    }
}
