package com.page.pulse.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the Rule entity's equals and hashCode methods.
 *
 * @author lewisjones
 */
class RuleTest
{

    /**
     * Helper method to create a Rule with a specific name.
     *
     * @param name the name to set
     * @return a Rule instance
     */
    private static Rule rule( final String name )
    {
        return new Rule( name, "Test description for " + name );
    }

    /**
     * Helper method to create a Rule with specific name and description.
     *
     * @param name        the name to set
     * @param description the description to set
     * @return a Rule instance
     */
    private static Rule rule( final String name, final String description )
    {
        return new Rule( name, description );
    }

    @Test
    void testEqualsAndHashCodeMatchForSameName()
    {
        // given
        final Rule first = rule( "test-rule" );
        final Rule second = rule( "test-rule" );

        // then
        assertThat( first ).isEqualTo( second );
        assertThat( first.hashCode() ).isEqualTo( second.hashCode() );
    }

    @Test
    void testEqualsAndHashCodeDifferForDifferentNames()
    {
        // given
        final Rule first = rule( "first-rule" );
        final Rule second = rule( "second-rule" );

        // then
        assertThat( first ).isNotEqualTo( second );
        assertThat( first.hashCode() ).isNotEqualTo( second.hashCode() );
    }

    @Test
    void testEqualsIgnoresDescriptionDifferences()
    {
        // given
        final Rule first = rule( "same-rule", "First description" );
        final Rule second = rule( "same-rule", "Second description" );

        // then
        assertThat( first ).isEqualTo( second );
        assertThat( first.hashCode() ).isEqualTo( second.hashCode() );
    }

    @Test
    void testEqualsWithNull()
    {
        // given
        final Rule rule = rule( "test-rule" );

        // then
        assertThat( rule ).isNotEqualTo( null );
    }

    @Test
    void testEqualsWithDifferentType()
    {
        // given
        final Rule rule = rule( "test-rule" );
        final String notARule = "not-a-rule";

        // then
        assertThat( rule ).isNotEqualTo( notARule );
    }

    @Test
    void testEqualsSymmetric()
    {
        // given
        final Rule first = rule( "test-rule" );
        final Rule second = rule( "test-rule" );

        // then
        assertThat( first ).isEqualTo( second );
        assertThat( second ).isEqualTo( first );
    }

    @Test
    void testEqualsTransitive()
    {
        // given
        final Rule first = rule( "test-rule" );
        final Rule second = rule( "test-rule" );
        final Rule third = rule( "test-rule" );

        // then
        assertThat( first ).isEqualTo( second );
        assertThat( second ).isEqualTo( third );
        assertThat( first ).isEqualTo( third );
    }

    @Test
    void testConstructorWithNameAndDescription()
    {
        // given
        final String name = "test-rule";
        final String description = "Test rule description";

        // when
        final Rule rule = new Rule( name, description );

        // then
        assertThat( rule.getName() ).isEqualTo( name );
        assertThat( rule.getDescription() ).isEqualTo( description );
        assertThat( rule.getId() ).isNull(); // ID should be null before persistence
    }

    @Test
    void testToString()
    {
        // given
        final Rule rule = rule( "test-rule", "Test description" );

        // when
        final String toString = rule.toString();

        // then
        assertThat( toString ).contains( "test-rule" );
        assertThat( toString ).contains( "Test description" );
    }

    @Test
    void testSettersAndGetters()
    {
        // given
        final Rule rule = new Rule( "original-name", "original-description" );

        // when
        rule.setId( 1 );
        rule.setName( "updated-name" );
        rule.setDescription( "updated-description" );

        // then
        assertThat( rule.getId() ).isEqualTo( 1 );
        assertThat( rule.getName() ).isEqualTo( "updated-name" );
        assertThat( rule.getDescription() ).isEqualTo( "updated-description" );
    }
}
