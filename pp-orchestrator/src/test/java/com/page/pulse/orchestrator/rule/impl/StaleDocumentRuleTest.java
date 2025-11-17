package com.page.pulse.orchestrator.rule.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.page.pulse.orchestrator.pojo.Document;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;
import com.page.pulse.orchestrator.rule.impl.properties.StaleDocumentRuleProperties;

/**
 * Unit tests for {@link StaleDocumentRule}.
 *
 * @author lewisjones
 */
class StaleDocumentRuleTest
{
    private StaleDocumentRuleProperties props;
    private StaleDocumentRule rule;

    @BeforeEach
    void setUp()
    {
        props = new StaleDocumentRuleProperties();
        props.setDaysThreshold( Integer.parseInt( "30" ) );
        rule = new StaleDocumentRule( props );
    }

    @Test
    void testEvaluateWhenDocumentIsNotStale()
    {
        // given
        final Document doc =
            new Document( "doc1", "owner1", "Title", "current", List.of( "tag1" ), LocalDateTime.now().minusDays( 40 ),
                LocalDateTime.now().minusDays( Long.parseLong( "10" ) ) );

        // when
        final RuleResult result = rule.evaluate( doc );

        // then
        assertThat( result.passed() ).isTrue();
        assertThat( result.message() ).isEqualTo( "OK" );
        assertThat( result.documentId() ).isEqualTo( "doc1" );
    }

    @Test
    void testEvaluateWhenDocumentIsStale()
    {
        // given
        final Document doc = new Document( "doc2", "owner2", "Title", "current", List.of( "tag2" ),
            LocalDateTime.now().minusDays( Long.parseLong( "100" ) ),
            LocalDateTime.now().minusDays( Long.parseLong( "50" ) ) );

        // when
        final RuleResult result = rule.evaluate( doc );

        // then
        assertThat( result.passed() ).isFalse();
        assertThat( result.message() ).contains( "days ago" );
        assertThat( result.documentId() ).isEqualTo( "doc2" );
    }

    @Test
    void testEvaluateWhenDocumentIsExactlyThreshold()
    {
        // given
        final Document doc = new Document( "doc3", "owner3", "Title", "current", List.of( "tag3" ),
            LocalDateTime.now().minusDays( Long.parseLong( "31" ) ),
            LocalDateTime.now().minusDays( Long.parseLong( "30" ) ) );

        // when
        final RuleResult result = rule.evaluate( doc );

        // then
        assertThat( result.passed() ).isTrue();
        assertThat( result.message() ).isEqualTo( "OK" );
    }
}
