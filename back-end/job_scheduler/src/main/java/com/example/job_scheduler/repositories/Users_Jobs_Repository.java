package com.example.job_scheduler.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.job_scheduler.entities.Users_Jobs;

import java.util.List;
@Repository
public interface Users_Jobs_Repository extends CrudRepository<Users_Jobs, Integer> {
    
    @Query(value = "SELECT * FROM USERS_JOBS WHERE ID = :id", nativeQuery = true)
    Users_Jobs getById(@Param("id") int id);

    @Query(value = "SELECT * FROM USERS_JOBS WHERE USER_ID = :id", nativeQuery = true)
    List<Users_Jobs> getByUser(@Param("id") int id);

    @Query(value = "SELECT * FROM USERS_JOBS WHERE JOB_ID = :id", nativeQuery = true)
    List<Users_Jobs> getByJob(@Param("id") int id);

    @Transactional@Modifying
    @Query(value = "INSERT INTO USERS_JOBS (user_id, job_id) VALUES (:user, :job)", nativeQuery = true)
    void insertJob(@Param("user") int mesaj, @Param("job") int job);

    @Transactional@Modifying
    @Query(value = "UPDATE USERS_JOBS SET user_id = :id2 WHERE user_id = :id", nativeQuery = true)
    void changeActive(@Param("id") int id, @Param("id2") int id2);
}
