package com.reba.personcrudapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reba.personcrudapi.exception.ContactRemoveNotAllowedException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.reba.personcrudapi.model.FamilyRelationship.*;

@Entity
@Table(name = "person", uniqueConstraints = { @UniqueConstraint(columnNames = { "document_type", "document_number", "country" }) })
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Getter
@Setter
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "document_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false)
    @EqualsAndHashCode.Include
    private String documentNumber;

    @Column(name = "country", nullable = false)
    @EqualsAndHashCode.Include
    private String country;

    @Column(name = "age")
    private int age;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Contact> contacts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Person parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Person> children = new HashSet<>();

    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.setPerson(this);
    }

    public void removeContact(Contact contact) throws ContactRemoveNotAllowedException {
        if (contacts.size() > 1)
            contacts.remove(contact);
        else
            throw new ContactRemoveNotAllowedException("Person must have at least one contact");
    }

    public void addChild(Person child) {
        children.add(child);
        child.setParent(this);
    }

    public boolean hasChild(Person child) {
        return children.contains(child);
    }

    public FamilyRelationship familyRelationship(@NotNull Person person) {
        if (isSiblingOf(person))
            return SIBLING;
        if (isCousinOf(person))
            return COUSIN;
        if (isUncleOf(person))
            return UNCLE;

        return UNFAMILIAR;
    }

    private boolean isSiblingOf(Person person) {
        try {
            return this.parent.equals(person.getParent());
        } catch (NullPointerException exception) {
            return false;
        }
    }

    private boolean isCousinOf(Person person) {
        try {
            return this.parent.getParent().hasChild(person.getParent());
        } catch (NullPointerException exception) {
            return false;
        }
    }

    private boolean isUncleOf(Person person) {
        try {
            return this.parent.equals(person.getParent().getParent());
        } catch (NullPointerException exception) {
            return false;
        }
    }
}
