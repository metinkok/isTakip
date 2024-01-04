package com.example.job_scheduler.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.job_scheduler.entities.Messages;
@Repository
public interface Messages_Repository extends CrudRepository<Messages, Integer>{

    @Query(value = "SELECT * FROM MESSAGES WHERE ID = :id", nativeQuery = true)
    Messages getById(@Param("id") int id);

    @Query(value = "SELECT * FROM MESSAGES WHERE MESAJ = :mesaj ORDER BY ID DESC LIMIT 1", nativeQuery = true)
    Messages getLastInsertedByMessage(@Param("mesaj") String mesaj);

    @Transactional@Modifying
    @Query(value = "INSERT INTO MESSAGES (mesaj, user_id) VALUES (:mesaj, :id)", nativeQuery = true)
    void insertMessage(@Param("mesaj") String mesaj, @Param("id") int id);
}
