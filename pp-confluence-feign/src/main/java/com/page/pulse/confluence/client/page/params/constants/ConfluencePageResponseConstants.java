package com.page.pulse.confluence.client.page.params.constants;

/**
 * Constants used in Confluence Pages responses.
 *
 * @author lewisjones
 */
public class ConfluencePageResponseConstants
{

    /**
     * Attribute name for page ID in Confluence Page API responses.
     */
    public static String PAGE_ID_ATTRIBUTE = "id";

    /**
     * Attribute name for page owner ID in Confluence Page API responses.
     */
    public static String PAGE_OWNER_ID_ATTRIBUTE = "ownerId";

    /**
     * Attribute name for page title.
     */
    public static final String PAGE_TITLE_ATTRIBUTE = "title";

    /**
     * Attribute name for page status (current, archived, etc.).
     */
    public static final String PAGE_STATUS_ATTRIBUTE = "status";

    /**
     * Attribute name for page created date.
     */
    public static final String PAGE_CREATED_AT_ATTRIBUTE = "createdAt";

    /**
     * Attribute name for page labels container.
     */
    public static final String PAGE_LABELS_ATTRIBUTE = "labels";

    /**
     * Attribute name for label results array.
     */
    public static final String LABELS_RESULTS_ATTRIBUTE = "results";

    /**
     * Attribute name for label name.
     */
    public static final String LABEL_NAME_ATTRIBUTE = "name";

    /**
     * Attribute name for version object.
     */
    public static final String VERSION_ATTRIBUTE = "version";

    /**
     * Attribute name for version created date (used as page updated date).
     */
    public static final String VERSION_CREATED_AT_ATTRIBUTE = "createdAt";

    /**
     * Private constructor to prevent instantiation.
     */
    private ConfluencePageResponseConstants()
    {
    }

}
