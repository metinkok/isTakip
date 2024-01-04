package com.example.job_scheduler.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.job_scheduler.entities.Messages_Jobs;
@Repository
public interface Messages_Jobs_Repository extends CrudRepository<Messages_Jobs, Integer>{
    
    @Query(value = "SELECT * FROM MESSAGES_JOBS WHERE ID = :id", nativeQuery = true)
    Messages_Jobs getById(@Param("id") int id);

    @Query(value = "SELECT * FROM MESSAGES_JOBS WHERE MESSAGE_ID = :id", nativeQuery = true)
    Messages_Jobs getByMessage(@Param("id") int id);

    @Query(value = "SELECT * FROM MESSAGES_JOBS WHERE JOB_ID = :id", nativeQuery = true)
    List<Messages_Jobs> getByJob(@Param("id") int id);

    @Transactional@Modifying
    @Query(value = "INSERT INTO MESSAGES_JOBS (mesaj_id, job_id) VALUES (:mesaj, :job)", nativeQuery = true)
    void insertMessageJob(@Param("mesaj") int mesaj, @Param("job") int job);
}