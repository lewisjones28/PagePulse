package com.page.pulse.orchestrator.rule.impl.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the Stale Document Rule.
 *
 * @author lewisjones
 */
@Configuration
@ConfigurationProperties( prefix = "document.rules.stale-document" )
public class StaleDocumentRuleProperties
{

    /**
     * Number of days after which a document is stale.
     */
    private int daysThreshold;

    /**
     * Gets the number of days after which a document is considered stale.
     *
     * @return the days threshold
     */
    public int getDaysThreshold()
    {
        return daysThreshold;
    }

    /**
     * Sets the number of days after which a document is considered stale.
     *
     * @param daysThreshold the days threshold to set
     */
    public void setDaysThreshold( final int daysThreshold )
    {
        this.daysThreshold = daysThreshold;
    }

}
