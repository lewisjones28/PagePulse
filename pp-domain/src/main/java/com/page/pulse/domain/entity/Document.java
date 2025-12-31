package com.page.pulse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JPA Entity representing a Document in the system.
 *
 * @author lewisjones
 */
@Entity
@Table( name = "documents", indexes = { @Index( name = "idx_documents_external_id", columnList = "external_id" ) } )
@Getter
@Setter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@EqualsAndHashCode( of = "externalId", callSuper = false )
@ToString
@AllArgsConstructor
public class Document extends Auditable<String>
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( name = "external_id", nullable = false, unique = true )
    private String externalId;

    @Column( name = "external_owner_id", nullable = false )
    private String externalOwnerId;

    @Column( nullable = false )
    private String title;

    @Column( nullable = false )
    private String status;

    @ElementCollection( fetch = FetchType.EAGER )
    private final List<String> tags = new ArrayList<>();

    @Column( name = "document_created_at" )
    private LocalDateTime documentLastCreatedAt;

    @Column( name = "document_updated_at" )
    private LocalDateTime documentLastUpdatedAt;

    public Document( final String externalId, final String externalOwnerId, final String title, final String status,
                     final Collection<String> tags, final LocalDateTime documentLastCreatedAt,
                     final LocalDateTime documentLastUpdatedAt )
    {
        this.externalId = externalId;
        this.externalOwnerId = externalOwnerId;
        this.title = title;
        this.status = status;
        if ( tags != null )
        {
            this.tags.addAll( tags );
        }
        this.documentLastCreatedAt = documentLastCreatedAt == null ? LocalDateTime.now() : documentLastCreatedAt;
        this.documentLastUpdatedAt = documentLastUpdatedAt == null ? LocalDateTime.now() : documentLastUpdatedAt;
    }
}
