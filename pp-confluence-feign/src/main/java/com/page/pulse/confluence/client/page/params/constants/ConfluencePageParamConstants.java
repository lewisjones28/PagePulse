package com.page.pulse.confluence.client.page.params.constants;

/**
 * Constants used in Confluence Pages client interactions.
 *
 * @author lewisjones
 */
public class ConfluencePageParamConstants
{

    /**
     * Private constructor to prevent instantiation.
     */
    private ConfluencePageParamConstants()
    {
    }

    /**
     * Parameter name to include labels in Confluence API requests.
     */
    public static String INCLUDE_LABELS_PARAM = "include-labels";

    /**
     * Parameter name for status filter in Confluence API requests.
     */
    public static String STATUS_PARAM = "status";

    /**
     * Status name for current (i.e., live) documents.
     */
    public static String CURRENT_STATUS_PARAM = "current";

    /**
     * Status name for archived documents.
     */
    public static String ARCHIVED_STATUS_PARAM = "archived";

    /**
     * Status name for draft documents.
     */
    public static String DRAFT_STATUS_PARAM = "draft";

}
