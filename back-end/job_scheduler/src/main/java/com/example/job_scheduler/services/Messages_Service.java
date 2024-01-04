package com.example.job_scheduler.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.entities.Messages;
import com.example.job_scheduler.entities.Messages_Jobs;
import com.example.job_scheduler.entities.Messages_Users;
import com.example.job_scheduler.repositories.Messages_Repository;

@Service
public class Messages_Service {
    @Autowired
    private Messages_Repository repository;
    @Autowired
    private Messages_Jobs_Service mj_service;
    @Autowired
    private Messages_Users_Service mu_service;

    //Bütün mesajları listeler
    public List<Messages> butunMesajlar(){
        List<Messages> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    //Idsi verilen mesajı getirir
    public Messages mesajiBul(int id){
        Messages message = repository.getById(id);
        return message;
    }

    //Yeni mesaj ekler ve bu mesajı ikili ilişki tablolarına gönderir
    public boolean mesajEkle(Messages message, int user_id, int job_id){
        repository.insertMessage(message.getMesaj(), user_id);
        message = repository.getLastInsertedByMessage(message.getMesaj());
        
        Messages_Jobs mj = new Messages_Jobs(message.getId(), job_id);
        Messages_Users mu = new Messages_Users(message.getId(), user_id);

        mj_service.mesajIsEkle(mj);
        mu_service.mesajKullaniciEkle(mu);
        return true;
    }
}
