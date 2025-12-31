package com.page.pulse.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the Document entity's equals and hashCode methods.
 *
 * @author lewisjones
 */
class DocumentTest
{

    /**
     * Helper method to create a Document with a specific externalId.
     *
     * @param externalId the external ID to set
     * @return a Document instance
     */
    private static Document document( final String externalId )
    {
        return new Document( externalId, "owner", "title", "status", List.of( "tag" ) );
    }

    @Test
    void testEqualsAndHashCodeMatchForSameExternalId()
    {
        // given
        final Document first = document( "external-1" );
        final Document second = document( "external-1" );

        // then
        assertThat( first ).isEqualTo( second );
        assertThat( first.hashCode() ).isEqualTo( second.hashCode() );
    }

    @Test
    void testEqualsAndHashCodeDifferForDifferentExternalIds()
    {
        // given
        final Document first = document( "external-1" );
        final Document second = document( "external-2" );

        // then
        assertThat( first ).isNotEqualTo( second );
        assertThat( first.hashCode() ).isNotEqualTo( second.hashCode() );
    }

}
