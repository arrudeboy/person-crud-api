package com.reba.personcrudapi.controller;

import com.reba.personcrudapi.dto.ContactDto;
import com.reba.personcrudapi.dto.PersonCreateDto;
import com.reba.personcrudapi.dto.PersonUpdateDto;
import com.reba.personcrudapi.exception.ContactNotFoundException;
import com.reba.personcrudapi.exception.ContactRemoveNotAllowedException;
import com.reba.personcrudapi.exception.PersonAlreadyExistsException;
import com.reba.personcrudapi.exception.PersonNotFoundException;
import com.reba.personcrudapi.model.Person;
import com.reba.personcrudapi.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.reba.personcrudapi.model.FamilyRelationship.UNFAMILIAR;

@RestController
@RequestMapping("/people")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<List<Person>> getAll() {

        logger.debug("Retrieving all people");
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getOne(@PathVariable long id) {

        logger.debug(String.format("Retrieving person with id %d", id));
        return personService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody PersonCreateDto personCreateDto) throws PersonAlreadyExistsException {

        logger.debug(String.format("Creating person %s", personCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(personCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable long id, @Valid @RequestBody PersonUpdateDto personUpdateDto) {

        logger.debug(String.format("Updating person with id %d", id));
        return personService.update(id, personUpdateDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {

        logger.debug(String.format("Deleting person with id %d", id));
        var deletedPerson = personService.delete(id);
        if (deletedPerson.isPresent())
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{parent_id}/parent/{child_id}")
    public ResponseEntity<Map<String, String>> parent(@PathVariable("parent_id") long parentId, @PathVariable("child_id") long childId)
            throws PersonNotFoundException {

        logger.debug(String.format("Linking person id %d as parent to person id %d", parentId, childId));
        if (parentId == childId) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Cannot be self-parent"));
        }
        var parent = personService.findById(parentId).orElseThrow(() -> new PersonNotFoundException("Person with that parent_id not found"));
        var child = personService.findById(childId).orElseThrow(() -> new PersonNotFoundException("Person with that child_id not found"));
        if (parent.hasChild(child)) {
            return ResponseEntity.ok(
                    Collections.singletonMap(
                            "message", String.format("Person id %d is already parent of person id %d", parentId, childId)));
        }
        if(child.hasChild(parent)) {
            return ResponseEntity.badRequest().body(
                    Collections.singletonMap(
                            "error", String.format("Person id %d is parent of person id %d", childId, parentId)));
        }

        if (parent.familyRelationship(child) != UNFAMILIAR || child.familyRelationship(parent) != UNFAMILIAR) {
            return ResponseEntity.badRequest().body(
                    Collections.singletonMap("error", "These people are already familiar"));
        }

        personService.addChildToParent(parent, child);
        return ResponseEntity.ok(Collections.singletonMap("message", String.format("Person id %d is now parent of person id %d", parentId, childId)));
    }

    @GetMapping("/relationships/{id1}/{id2}")
    public ResponseEntity<Map<String, String>> relationship(@PathVariable("id1") long personId1, @PathVariable("id2") long personId2) throws PersonNotFoundException {

        logger.debug(String.format("Retrieving relationship between person id %d and person id %d", personId1, personId2));
        var person1 = personService.findById(personId1).orElseThrow(() -> new PersonNotFoundException(String.format("Person with id %d not found", personId1)));
        var person2 = personService.findById(personId2).orElseThrow(() -> new PersonNotFoundException(String.format("Person with id %d not found", personId2)));

        String messageTemplate = "Person id %d is %s of person id %d";
        String message;
        if (person1.hasChild(person2))
            message = String.format(messageTemplate, personId1, "PARENT", personId2);
        else if (person2.hasChild(person1))
            message = String.format(messageTemplate, personId1, "CHILD", personId2);
        else
            message = String.format("Person id %d is %s of person id %d", personId1, person1.familyRelationship(person2).name(), personId2);

        return ResponseEntity.ok(Collections.singletonMap("message", message));
    }

    @PostMapping("/{person_id}/contacts")
    public ResponseEntity<Person> addContact(@PathVariable("person_id") long personId, @Valid @RequestBody ContactDto contactDto) throws PersonNotFoundException {

        logger.debug(String.format("Adding contact to person id %d", personId));
        var person = personService.findById(personId).orElseThrow(() -> new PersonNotFoundException(String.format("Person with id %d not found", personId)));
        return ResponseEntity.ok(personService.addContact(person, contactDto));
    }

    @DeleteMapping("/{person_id}/contacts/{contact_id}")
    public ResponseEntity<Person> deleteContact(@PathVariable("person_id") long personId, @PathVariable("contact_id") long contactId) throws PersonNotFoundException, ContactNotFoundException, ContactRemoveNotAllowedException {

        logger.debug(String.format("Deleting contact id %d to person id %d", contactId, personId));
        var person = personService.findById(personId).orElseThrow(() -> new PersonNotFoundException(String.format("Person with id %d not found", personId)));
        var contact = person.getContacts().stream()
                .filter(c -> c.getId() == contactId).findFirst()
                .orElseThrow(() -> new ContactNotFoundException(String.format("Contact id %d not found in person id %d", contactId, personId)));

        return ResponseEntity.ok(personService.removeContact(person, contact));
    }

    @PutMapping("/{person_id}/contacts/{contact_id}")
    public ResponseEntity<Person> updateContact(
            @PathVariable("person_id") long personId, @PathVariable("contact_id") long contactId,
            @Valid @RequestBody ContactDto contactDto) throws PersonNotFoundException, ContactNotFoundException {

        logger.debug(String.format("Updating contact id %d to person id %d", contactId, personId));
        var person = personService.findById(personId).orElseThrow(() -> new PersonNotFoundException(String.format("Person with id %d not found", personId)));
        if (person.getContacts().stream().noneMatch(c -> c.getId() == contactId)) {
            throw new ContactNotFoundException(String.format("Contact id %d not found in person id %d", contactId, personId));
        }

        return ResponseEntity.ok(personService.updateContact(person, contactId, contactDto));
    }
}
