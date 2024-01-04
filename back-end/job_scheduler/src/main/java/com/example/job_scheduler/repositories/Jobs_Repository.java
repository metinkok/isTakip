package com.example.job_scheduler.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.job_scheduler.entities.Jobs;
@Repository
public interface Jobs_Repository extends CrudRepository<Jobs, Integer> {
    
    @Query(value = "SELECT * FROM JOBS WHERE STATUS <= 3 AND PARENT IS NULL ORDER BY ID", nativeQuery = true)
    List<Jobs> allActive();

    @Query(value = "SELECT * FROM JOBS WHERE STATUS = 4 AND PARENT IS NULL ORDER BY DEADLINE", nativeQuery = true)
    List<Jobs> allInactive();

    @Query(value = "SELECT * FROM JOBS WHERE PARENT = :id", nativeQuery = true)
    List<Jobs> subJobs(@Param("id") int id);

    @Query(value = "SELECT * FROM JOBS WHERE STATUS = :status", nativeQuery = true)
    List<Jobs> getWithStatus(@Param("status") int status);

    @Query(value = "SELECT * FROM JOBS WHERE DEADLINE = :deadline", nativeQuery = true)
    List<Jobs> getToday(@Param("deadline") String deadline);

    @Query(value = "SELECT * FROM JOBS WHERE DEADLINE >= :deadline", nativeQuery = true)
    List<Jobs> getDeadline(@Param("deadline") String deadline);

    @Query(value = "SELECT * FROM JOBS WHERE DEADLINE like :deadline%", nativeQuery = true)
    List<Jobs> getDeadlineMonth(@Param("deadline") String deadline);

    @Query(value = "SELECT * FROM JOBS WHERE ISIM LIKE %:isim%", nativeQuery = true)
    List<Jobs> searchLike(@Param("isim") String isim);

    @Query(value = "SELECT * FROM JOBS WHERE ID = :id", nativeQuery = true)
    Jobs getById(@Param("id") int id);

    @Transactional@Modifying
    @Query(value = "INSERT INTO JOBS (isim, aciklama, status, point, deadline) VALUES (:isim, :aciklama, :status, :point, :deadline)", nativeQuery = true)
    void insertJob(@Param("isim") String isim, @Param("aciklama") String aciklama, @Param("status") int status, @Param("point") int point, @Param("deadline") String deadline);

    @Transactional@Modifying
    @Query(value = "INSERT INTO JOBS (isim, aciklama, status, point, deadline, parent) VALUES (:isim, :aciklama, :status, :point, :deadline,:parent)", nativeQuery = true)
    void insertSubJob(@Param("isim") String isim, @Param("aciklama") String aciklama, @Param("status") int status, @Param("point") int point, @Param("deadline") String deadline, @Param("parent") int parent);

    @Transactional@Modifying
    @Query(value = "UPDATE JOBS SET status = :status WHERE ID = :id", nativeQuery = true)
    void updateStatus(@Param("status") int status, @Param("id") int id);

    @Transactional@Modifying
    @Query(value = "UPDATE JOBS SET deadline = :deadline WHERE ID = :id", nativeQuery = true)
    void updateDeadline(@Param("deadline") String deadline, @Param("id") int id);

    @Transactional@Modifying
    @Query(value = "UPDATE JOBS SET point = :point WHERE ID = :id", nativeQuery = true)
    void updatePoint(@Param("point") int point, @Param("id") int id);
}
