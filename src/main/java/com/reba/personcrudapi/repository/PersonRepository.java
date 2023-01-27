package com.reba.personcrudapi.repository;

import com.reba.personcrudapi.model.DocumentType;
import com.reba.personcrudapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByDocumentNumberAndDocumentTypeAndCountry(String documentNumber, DocumentType documentType, String country);
    Optional<Person> findByDocumentNumberAndDocumentTypeAndCountry(String documentNumber, DocumentType documentType, String country);
}
