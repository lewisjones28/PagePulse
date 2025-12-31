package com.page.pulse.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JPA Entity representing a Document in the system.
 *
 * @author lewisjones
 */
@Entity
@Table( name = "documents" )
@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@EqualsAndHashCode( of = "externalId", callSuper = false )
public class Document extends Auditable<String>
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotBlank
    @Column( name = "external_id", nullable = false, unique = true )
    private String externalId;

    @NotBlank
    @Column( name = "external_owner_id", nullable = false )
    private String externalOwnerId;

    @NotBlank
    @Column( nullable = false )
    private String title;

    @NotBlank
    @Column( nullable = false )
    private String status;

    @ElementCollection( fetch = FetchType.EAGER )
    private final List<String> tags = new ArrayList<>();

    public Document( final String externalId, final String externalOwnerId, final String title, final String status,
                     final Collection<String> tags )
    {
        this.externalId = externalId;
        this.externalOwnerId = externalOwnerId;
        this.title = title;
        this.status = status;
        if ( tags != null )
        {
            this.tags.addAll( tags );
        }
    }
}
