package com.example.job_scheduler.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.entities.Messages_Users;
import com.example.job_scheduler.repositories.Messages_Users_Repository;

@Service
public class Messages_Users_Service {
    @Autowired
    private Messages_Users_Repository repository;

    //Bütün mesaj-kullanıcı ikililerini listeler
    public List<Messages_Users> butunMesajlKullanicilar(){
        List<Messages_Users> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    //Idsi verilen mesaj-kullanıcı ikilisini getirir
    public Messages_Users mesajKullaniciyiBul(int id){
        Messages_Users mu = repository.getById(id);
        return mu;
    }

    //Idsi verilen mesajı gönderen kullanıcıyı getirir
    public Messages_Users mesajdanBul(int id){
        Messages_Users mu = repository.getByMessage(id);
        return mu;
    }

    //Idsi verilen kullanıcının gönderdiği bütün mesajları getirir
    public List<Messages_Users> kullanicidanBul(int id){
        List<Messages_Users> mu = new ArrayList<>();
        repository.getByUser(id).forEach(mu::add);;
        return mu;
    }

    //Yeni mesaj-kullanıcı ikilisi ekler
    public boolean mesajKullaniciEkle(Messages_Users mu){
        repository.insertMessageUser(mu.getMesaj_id(), mu.getUser_id());
        return true;
    }
}
