package com.example.job_scheduler.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.job_scheduler.entities.Messages_Users;

import java.util.List;
@Repository
public interface Messages_Users_Repository extends CrudRepository<Messages_Users, Integer>{
    
    @Query(value = "SELECT * FROM MESSAGES_USERS WHERE ID = :id", nativeQuery = true)
    Messages_Users getById(@Param("id") int id);

    @Query(value = "SELECT * FROM MESSAGES_USERS WHERE MESSAGE_ID = :id", nativeQuery = true)
    Messages_Users getByMessage(@Param("id") int id);

    @Query(value = "SELECT * FROM MESSAGES_USERS WHERE USER_ID = :id", nativeQuery = true)
    List<Messages_Users> getByUser(@Param("id") int id);

    @Transactional@Modifying
    @Query(value = "INSERT INTO MESSAGES_USERS (mesaj_id, user_id) VALUES (:mesaj, :job)", nativeQuery = true)
    void insertMessageUser(@Param("mesaj") int mesaj, @Param("job") int user);

    @Transactional@Modifying
    @Query(value = "UPDATE MESSAGES_USERS SET user_id = :id2 WHERE user_id = :id", nativeQuery = true)
    void changeActive(@Param("id") int id, @Param("id2") int id2);
}