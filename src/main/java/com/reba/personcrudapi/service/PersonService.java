package com.reba.personcrudapi.service;

import com.reba.personcrudapi.dto.ContactDto;
import com.reba.personcrudapi.dto.PersonCreateDto;
import com.reba.personcrudapi.dto.PersonUpdateDto;
import com.reba.personcrudapi.exception.ContactRemoveNotAllowedException;
import com.reba.personcrudapi.exception.PersonAlreadyExistsException;
import com.reba.personcrudapi.model.Contact;
import com.reba.personcrudapi.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> findAll();
    Optional<Person> findById(long id);
    Person create(PersonCreateDto personCreateDto) throws PersonAlreadyExistsException;
    Optional<Person> update(long id, PersonUpdateDto personUpdateDto);
    Optional<Person> delete(long id);
    void addChildToParent(Person parent, Person child);
    Person addContact(Person person, ContactDto contactDto);
    Person removeContact(Person person, Contact contact) throws ContactRemoveNotAllowedException;
    Person updateContact(Person person, long contactId, ContactDto contactDto);
}
