package com.page.pulse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity representing a Document-Rule violation mapping in the system.
 * This entity tracks whether a document is violating a specific rule.
 *
 * @author lewisjones
 */
@Entity
@Table( name = "document_rule_violations",
        indexes = {
            @Index( name = "idx_document_rule_violations_document_id", columnList = "document_id" ),
            @Index( name = "idx_document_rule_violations_rule_id", columnList = "rule_id" ),
            @Index( name = "idx_document_rule_violations_violating", columnList = "violating" )
        },
        uniqueConstraints = {
            @UniqueConstraint( name = "uk_document_rule_violations_document_rule",
                              columnNames = { "document_id", "rule_id" } )
        } )
@Getter
@Setter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@EqualsAndHashCode( of = { "document", "rule" }, callSuper = false )
@ToString( exclude = { "document", "rule" } )
@AllArgsConstructor
public class DocumentRuleViolation extends Auditable<String>
{
    /** Maximum length for violation details text. */
    private static final int VIOLATION_DETAILS_MAX_LENGTH = 1000;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "document_id", nullable = false )
    private Document document;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "rule_id", nullable = false )
    private Rule rule;

    @Column( nullable = false )
    private Boolean violating;

    @Column( name = "violation_details", length = VIOLATION_DETAILS_MAX_LENGTH )
    private String violationDetails;

    public DocumentRuleViolation( final Document document, final Rule rule, final Boolean violating )
    {
        this.document = document;
        this.rule = rule;
        this.violating = violating != null ? violating : false;
    }

    public DocumentRuleViolation( final Document document, final Rule rule, final Boolean violating,
                                  final String violationDetails )
    {
        this.document = document;
        this.rule = rule;
        this.violating = violating != null ? violating : false;
        this.violationDetails = violationDetails;
    }
}
