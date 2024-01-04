package com.example.job_scheduler.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.entities.Users;
import com.example.job_scheduler.repositories.Users_Repository;

@Service
public class Users_Service {
    @Autowired
    private Users_Repository repository;

    //Bütün Kullanıcıları listeler
    public List<Users> butunKullanicilarAdmin(){
        List<Users> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    //Bütün aktif kullanıcıları listeler
    public List<Users> butunKullanicilar(){
        List<Users> list = new ArrayList<>();
        repository.findActive().forEach(list::add);
        return list;
    }

    //Idsi verilen kullanıcıyı getirir
    public Users kullaniciyiBul(int id){
        Users user = repository.getById(id);
        return user;
    }

    //İsmi içerisinde verilen string geçen kullanıcıları listeler
    public List<Users> isimleAra(String isim){
        List<Users> list = new ArrayList<>();
        repository.searchLike(isim).forEach(list::add);
        return list;
    }

    //E-posta adresi verilen kullanıcıyı bulur
    public Users kullaniciyiBul(String e_posta){
        Users user = repository.getByMail(e_posta);
        return user;
    }

    //Yeni kullanıcı ekler
    public boolean kullaniciEkle(Users user){
        repository.insertUser(user.getIsim(), user.getE_posta(), user.getSifre());
        return true;
    }

    //Idsi verilen kullanıcının şifresini günceller(Buraya gelen şifrenin hashli olduğu varsayılır)
    public boolean sifreDegistir(int id, String sifre){
        repository.changePassword(sifre, id);
        return true;
    }

    //Idsi verilen kullanıcıya admin yetkisi(1) verir
    public boolean adminYap(int id){
        repository.giveAdmin(1, id);
        return true;
    }

    //Idsi verilen kullanıcıdan admin yetkisini alır
    public boolean adminlikAl(int id){
        repository.giveAdmin(-1, id);
        return true;
    }

    //Adminleri listeler
    public List<Users> adminleriBul(){
        return repository.getAdmin(1);
    }

    //Verilen kullanıcının puanını günceller
    public boolean puaniGuncelle(int puan, Users user){
        repository.updatePoint(puan, user.getE_posta());
        return true;
    }

    //Bütün puanları 0 yapar
    public boolean puanlariResetle(){
        repository.resetPoints();
        return true;
    }

    //IDsi verilen kullanıcının aktiflik durumunu değiştirir
    public boolean aktfilikDegistir(int id){
        Users user = repository.getById(id);
        int active = user.getActive()*-1;
        repository.changeActive(id, active);
        return true;
    }
}