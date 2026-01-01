package com.page.pulse.orchestrator.alert;

/**
 * Represents a destination capable of receiving alert payloads.
 *
 * @author lewisjones
 */
public interface AlertChannel
{
    /**
     * Sends the provided payload to the underlying alert transport.
     *
     * @param payload the alert payload to send
     */
    void send( AlertPayload payload );
}

