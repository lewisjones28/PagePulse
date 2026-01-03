package com.page.pulse.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity representing a Rule in the system.
 *
 * @author lewisjones
 */
@Entity
@Table( name = "rules" )
@Getter
@Setter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@EqualsAndHashCode( of = "name", callSuper = false )
@ToString
@AllArgsConstructor
public class Rule extends Auditable<String>
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column( nullable = false, unique = true )
    private String name;

    @Column( nullable = false )
    private String description;

    @Column( nullable = false )
    private Boolean active = true;

    public Rule( final String name, final String description )
    {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    public Rule( final String name, final String description, final Boolean active )
    {
        this.name = name;
        this.description = description;
        this.active = active != null ? active : true;
    }
}
