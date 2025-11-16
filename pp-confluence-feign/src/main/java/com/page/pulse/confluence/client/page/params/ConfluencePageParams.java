package com.page.pulse.confluence.client.page.params;

import com.page.pulse.confluence.client.page.params.constants.ConfluencePageParamConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private List<String> status;

    /**
     * Default constructor is private to enforce use of factory method.
     */
    public ConfluencePageParams()
    {
    }

    /**
     * Obtain a new empty params instance. Prefer using this factory in other modules
     * to make intent explicit and avoid constructor-call issues in cross-module analysis.
     *
     * @return new empty ConfluencePageParams instance
     */
    public static ConfluencePageParams empty()
    {
        return new ConfluencePageParams();
    }

    /**
     * Set whether to include labels in the response.
     *
     * @param includeLabels true to include labels, false to exclude, null for default behavior
     * @return this params instance for fluent chaining
     */
    public ConfluencePageParams includeLabels( final Boolean includeLabels )
    {
        this.includeLabels = includeLabels;
        return this;
    }

    /**
     * Set the desired page status values (e.g. current, archived, trashed, deleted, historical, draft).
     *
     * @param statuses list of status values, or null to clear
     * @return this params instance for fluent chaining
     */
    public ConfluencePageParams status( final List<String> statuses )
    {
        if ( statuses == null )
        {
            this.status = null;
        }
        else
        {
            this.status = new ArrayList<>( statuses );
        }
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
            map.put( ConfluencePageParamConstants.INCLUDE_LABELS_PARAM, includeLabels );
        }
        if ( status != null && !status.isEmpty() )
        {
            // Confluence accepts multiple status values; send as comma-separated list
            final String joined = status.stream().map( String::trim ).collect( Collectors.joining( "," ) );
            map.put( ConfluencePageParamConstants.STATUS_PARAM, joined );
        }
        return map;
    }

    /**
     * Check if no parameters have been set.
     *
     * @return true if no parameters are set, false otherwise
     */
    public boolean isEmpty()
    {
        return toMap().isEmpty();
    }

    /**
     * String representation of the parameters for debugging.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "ConfluencePageParams{" + toMap().toString() + '}';
    }
}
