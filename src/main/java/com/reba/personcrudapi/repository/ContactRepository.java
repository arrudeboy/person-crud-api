package com.reba.personcrudapi.repository;

import com.reba.personcrudapi.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
