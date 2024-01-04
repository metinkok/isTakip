package com.example.job_scheduler.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.job_scheduler.entities.Logs;
@Repository
public interface Logs_Repository extends CrudRepository<Logs, Integer>{

    @Query(value = "SELECT * FROM LOGS ORDER BY ID DESC", nativeQuery = true)
    List<Logs> all();

    @Query(value = "SELECT * FROM LOGS WHERE DATE_TIME like :date_time% ORDER BY ID DESC", nativeQuery = true)
    List<Logs> getToday(@Param("date_time") String date_time);

    @Query(value = "SELECT * FROM LOGS WHERE DATE_TIME like :date_time% ORDER BY ID DESC", nativeQuery = true)
    List<Logs> getThisMonth(@Param("date_time") String date_time);

    @Query(value = "SELECT * FROM LOGS WHERE DATE_TIME like :date_time% ORDER BY ID DESC", nativeQuery = true)
    List<Logs> getThisYear(@Param("date_time") String date_time);

    @Query(value = "SELECT * FROM LOGS WHERE (DATE_TIME > :date_time1 AND DATE_TIME < :date_time2) OR DATE_TIME LIKE :date_time2% ORDER BY ID DESC", nativeQuery = true)
    List<Logs> getBetween(@Param("date_time1") String date_time1, @Param("date_time2") String date_time2);

    @Query(value = "SELECT * FROM LOGS WHERE E_POSTA = :e_posta ORDER BY ID DESC", nativeQuery = true)
    List<Logs> getByUser(@Param("e_posta") String e_posta);

    @Transactional@Modifying
    @Query(value = "INSERT INTO LOGS (log_record, e_posta, date_time) VALUES (:log_record, :e_posta, :date_time)", nativeQuery = true)
    void insertJob(@Param("log_record") String log_record, @Param("e_posta") String e_posta, @Param("date_time") String date_time);
}