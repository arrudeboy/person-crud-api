package com.reba.personcrudapi.service;

import com.reba.personcrudapi.dto.ContactDto;
import com.reba.personcrudapi.dto.PersonCreateDto;
import com.reba.personcrudapi.dto.PersonUpdateDto;
import com.reba.personcrudapi.exception.ContactRemoveNotAllowedException;
import com.reba.personcrudapi.exception.PersonAlreadyExistsException;
import com.reba.personcrudapi.model.*;
import com.reba.personcrudapi.repository.ContactRepository;
import com.reba.personcrudapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonJpaService implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(long id) {
        return personRepository.findById(id);
    }

    @Override
    public Person create(PersonCreateDto personCreateDto) throws PersonAlreadyExistsException {

        var documentType = DocumentType.valueOf(personCreateDto.getDocumentType());
        var documentNumber = personCreateDto.getDocumentNumber();
        var country = personCreateDto.getCountry();
        if (personRepository.existsByDocumentNumberAndDocumentTypeAndCountry(documentNumber, documentType, country))
            throw new PersonAlreadyExistsException("A person with same document_number, document_type and country already exists");

        var person = new Person();
        person.setDocumentType(documentType);
        person.setDocumentNumber(documentNumber);
        person.setCountry(capitalizeFirstLetter(country));
        person.setAge(personCreateDto.getAge());
        person.setFirstName(personCreateDto.getFirstName());
        person.setLastName(personCreateDto.getLastName());
        person.setContacts(personCreateDto.getContacts()
                .stream().map(c -> {
                    var contact = new Contact();
                    contact.setContactType(ContactType.valueOf(c.getType()));
                    contact.setValue(c.getValue());
                    contact.setPerson(person);
                    return contact;
                }).collect(Collectors.toSet()));

        return personRepository.save(person);
    }

    @Override
    public Optional<Person> update(long id, PersonUpdateDto personUpdateDto) {

        var optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            var person = optionalPerson.get();
            person.setDocumentType(DocumentType.valueOf(personUpdateDto.getDocumentType()));
            person.setDocumentNumber(personUpdateDto.getDocumentNumber());
            person.setCountry(capitalizeFirstLetter(personUpdateDto.getCountry()));
            person.setAge(personUpdateDto.getAge());
            person.setFirstName(personUpdateDto.getFirstName());
            person.setLastName(personUpdateDto.getLastName());

            return Optional.of(personRepository.save(person));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Person> delete(long id) {
        var optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            var person = optionalPerson.get();
            personRepository.delete(person);
            return Optional.of(person);
        }

        return Optional.empty();
    }

    @Override
    public void addChildToParent(Person parent, Person child) {
        parent.addChild(child);
        personRepository.save(parent);
    }

    @Override
    public Person addContact(Person person, ContactDto contactDto) {
        var contact = new Contact();
        contact.setContactType(ContactType.valueOf(contactDto.getType()));
        contact.setValue(contactDto.getValue());
        person.addContact(contact);
        return personRepository.save(person);
    }

    @Override
    public Person removeContact(Person person, Contact contact) throws ContactRemoveNotAllowedException {
        person.removeContact(contact);
        personRepository.saveAndFlush(person);
        contactRepository.delete(contact);
        return person;
    }

    @Override
    public Person updateContact(Person person, long contactId, ContactDto contactDto) {
        person.getContacts().stream()
                .filter(c -> c.getId() == contactId)
                .findFirst().ifPresent(contact -> {
                    contact.setContactType(ContactType.valueOf(contactDto.getType()));
                    contact.setValue(contactDto.getValue());
                    personRepository.saveAndFlush(person);
        });

        return person;
    }

    private String capitalizeFirstLetter(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
