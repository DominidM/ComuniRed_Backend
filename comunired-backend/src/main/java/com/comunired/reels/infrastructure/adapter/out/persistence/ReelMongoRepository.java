package com.comunired.reels.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReelMongoRepository extends MongoRepository<ReelDocument, String> {
    List<ReelDocument> findByActivoTrue();
    List<ReelDocument> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String author, String description);
}
