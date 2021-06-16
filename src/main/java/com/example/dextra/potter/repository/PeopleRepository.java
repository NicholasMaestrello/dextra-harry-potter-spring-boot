package com.example.dextra.potter.repository;

import com.example.dextra.potter.model.document.PeopleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends MongoRepository<PeopleDocument, String> {
}
