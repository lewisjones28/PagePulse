package com.page.pulse.orchestrator.rule.impl;

import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.pojo.rule.RuleResult;
import com.page.pulse.orchestrator.rule.impl.properties.StaleDocumentRuleProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link StaleDocumentRule}.
 *
 * @author lewisjones
 */
class StaleDocumentDtoRuleTest
{
    private StaleDocumentRule rule;

    @BeforeEach
    void setUp()
    {
        final StaleDocumentRuleProperties props = new StaleDocumentRuleProperties();
        props.setDaysThreshold( Integer.parseInt( "30" ) );
        rule = new StaleDocumentRule( props );
    }

    @Test
    void testEvaluateWhenDocumentIsNotStale()
    {
        // given
        final DocumentDto doc = new DocumentDto( "doc1", "owner1", "Title", "current", List.of( "tag1" ),
            LocalDateTime.now().minusDays( 40 ),
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
        final DocumentDto doc = new DocumentDto( "doc2", "owner2", "Title", "current", List.of( "tag2" ),
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
        final DocumentDto doc = new DocumentDto( "doc3", "owner3", "Title", "current", List.of( "tag3" ),
            LocalDateTime.now().minusDays( Long.parseLong( "31" ) ),
            LocalDateTime.now().minusDays( Long.parseLong( "30" ) ) );

        // when
        final RuleResult result = rule.evaluate( doc );

        // then
        assertThat( result.passed() ).isTrue();
        assertThat( result.message() ).isEqualTo( "OK" );
    }
}
