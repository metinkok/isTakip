package com.example.job_scheduler.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.entities.Messages_Jobs;
import com.example.job_scheduler.repositories.Messages_Jobs_Repository;

@Service
public class Messages_Jobs_Service {
    @Autowired
    private Messages_Jobs_Repository repository;

    //Bütün mesaj-iş ikililerini getirir
    public List<Messages_Jobs> butunMesajlarIsler(){
        List<Messages_Jobs> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    //Idsi verilen mesajı bulur
    public Messages_Jobs mesajIsiBul(int id){
        Messages_Jobs mj = repository.getById(id);
        return mj;
    }

    //Idsi verilen mesajın yapıldığı işi bulur
    public Messages_Jobs mesajdanBul(int id){
        Messages_Jobs mj = repository.getByMessage(id);
        return mj;
    }

    //Idsi verilen işe gönderilmiş bütün mesajları listeler
    public List<Messages_Jobs> isdenBul(int id){
        List<Messages_Jobs> mj = new ArrayList<>();
        repository.getByJob(id).forEach(mj::add);
        return mj;
    }

    //Yeni mesaj ekler
    public boolean mesajIsEkle(Messages_Jobs mj){
        repository.insertMessageJob(mj.getMesaj_id(), mj.getJob_id());
        return true;
    }
}
