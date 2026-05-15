package com.comunired.reels.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReelComentarioMongoRepository extends MongoRepository<ReelComentarioDocument, String> {
    List<ReelComentarioDocument> findByReelIdOrderByFechaCreacionAsc(String reelId);
    int countByReelId(String reelId);
}
