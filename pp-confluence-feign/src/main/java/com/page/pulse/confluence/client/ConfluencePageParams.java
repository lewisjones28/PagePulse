package com.page.pulse.confluence.client;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parameter object to hold optional query parameters for Confluence page requests.
 * Build using the fluent setters and call {@link #toMap()} to obtain a map suitable
 * for passing to Feign's @QueryMap parameter.
 *
 * @author lewisjones
 */
public class ConfluencePageParams
{
    private Boolean includeLabels;

    public ConfluencePageParams()
    {
    }

    public ConfluencePageParams includeLabels( final Boolean includeLabels )
    {
        this.includeLabels = includeLabels;
        return this;
    }

    /**
     * Convert the parameters to a map suitable for Feign's @QueryMap.
     *
     * @return map of query parameters
     */
    public Map<String, Object> toMap()
    {
        final Map<String, Object> map = new LinkedHashMap<>();
        if ( includeLabels != null )
        {
            map.put( "include-labels", includeLabels );
        }
        return map;
    }

    public boolean isEmpty()
    {
        return toMap().isEmpty();
    }
}

