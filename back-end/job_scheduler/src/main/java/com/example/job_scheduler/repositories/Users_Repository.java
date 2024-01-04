package com.example.job_scheduler.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

import com.example.job_scheduler.entities.Users;
@Repository
public interface Users_Repository extends CrudRepository<Users, Integer> {
    
    @Query(value = "SELECT * FROM USERS WHERE ID = :id", nativeQuery = true)
    Users getById(@Param("id") int id);

    @Query(value = "SELECT * FROM USERS WHERE ACTIVE > 0", nativeQuery = true)
    List<Users> findActive();

    @Query(value = "SELECT * FROM USERS WHERE ADMN = :admin", nativeQuery = true)
    List<Users> getAdmin(@Param("admin") int admin);

    @Query(value = "SELECT * FROM USERS WHERE ISIM LIKE %:isim% ORDER BY ISIM DESC", nativeQuery = true)
    List<Users> searchLike(@Param("isim") String isim);

    @Query(value = "SELECT * FROM USERS WHERE E_POSTA = :e_posta", nativeQuery = true)
    Users getByMail(@Param("e_posta") String e_posta);

    @Transactional@Modifying
    @Query(value = "INSERT INTO USERS (isim, e_posta, sifre, point, admn, active) VALUES (:isim, :e_posta, :sifre, 0, -1, 1)", nativeQuery = true)
    void insertUser(@Param("isim") String isim, @Param("e_posta") String e_posta, @Param("sifre") String sifre);

    @Transactional@Modifying
    @Query(value = "UPDATE USERS SET SIFRE = :sifre WHERE ID = :id", nativeQuery = true)
    void changePassword(@Param("sifre") String sifre, @Param("id") int id);

    @Transactional@Modifying
    @Query(value = "UPDATE USERS SET ADMN = :admin WHERE ID = :id", nativeQuery = true)
    void giveAdmin(@Param("admin") int admin, @Param("id") int id);

    @Transactional@Modifying
    @Query(value = "UPDATE USERS SET POINT = :point WHERE E_POSTA = :e_posta", nativeQuery = true)
    void updatePoint(@Param("point") int point, @Param("e_posta") String e_posta);

    @Transactional@Modifying
    @Query(value = "UPDATE USERS SET POINT = 0", nativeQuery = true)
    void resetPoints();

    @Transactional@Modifying
    @Query(value = "UPDATE USERS SET ACTIVE = :active WHERE ID= :id", nativeQuery = true)
    void changeActive(@Param("id") int id, @Param("active") int active);
}
