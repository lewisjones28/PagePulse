package com.page.pulse.orchestrator.rule.engine;

import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.pojo.rule.RuleEvaluation;
import com.page.pulse.orchestrator.rule.impl.StaleDocumentRule;
import com.page.pulse.orchestrator.rule.impl.properties.StaleDocumentRuleProperties;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DocumentRuleEngine}.
 *
 * @author lewisjones
 */
class DocumentRuleEngineTest
{
    private static DocumentDto sampleDocument()
    {
        return new DocumentDto( "doc-123", "owner", "Title", "current", List.of( "tag" ),
            LocalDateTime.now().minusDays( 5 ), LocalDateTime.now() );
    }

    @Test
    void testEvaluateInvokesAllRulesAndReturnsEvaluations()
    {
        // given
        final DocumentDto document = sampleDocument();
        final StaleDocumentRuleProperties props = new StaleDocumentRuleProperties();
        props.setDaysThreshold( Integer.parseInt( "30" ) );
        final StaleDocumentRule staleRule = new StaleDocumentRule( props );
        final DocumentRuleEngine engine = new DocumentRuleEngine( List.of( staleRule ) );

        // when
        final List<RuleEvaluation> evaluations = engine.evaluate( document );

        // then
        assertThat( evaluations ).hasSize( 1 );
        assertThat( evaluations.getFirst().ruleName() ).isEqualTo( "stale-document-rule" );
    }

    @Test
    void testEvaluateReturnsEmptyListWhenNoRulesRegistered()
    {
        // given
        final DocumentRuleEngine engine = new DocumentRuleEngine( List.of() );

        // when
        final List<RuleEvaluation> evaluations = engine.evaluate( sampleDocument() );

        // then
        assertThat( evaluations ).isEmpty();
    }
}
