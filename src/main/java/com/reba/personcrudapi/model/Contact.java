package com.reba.personcrudapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contact")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    @JsonIgnore
    private Person person;
}
