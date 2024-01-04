package com.example.job_scheduler.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.entities.Users_Jobs;
import com.example.job_scheduler.entities.Messages;
import com.example.job_scheduler.repositories.Users_Jobs_Repository;

@Service
public class Users_Jobs_Service {
    @Autowired
    private Users_Jobs_Repository repository;
    @Autowired
    private Messages_Service m_service;

    //Bütün kullanıcı-iş ikililerini listeler
    public List<Users_Jobs> butunKullaniciIsler(){
        List<Users_Jobs> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    //Idsi verilen kullanıcı-iş ikilisini bulur
    public Users_Jobs kullaniciIsiBul(int id){
        Users_Jobs uj = repository.getById(id);
        return uj;
    }

    //Idsi verilen kullanıcının bütün işlerini getirir
    public List<Users_Jobs> KullanicidanBul(int id){
        List<Users_Jobs> uj = new ArrayList<>();
        repository.getByUser(id).forEach(uj:: add);
        return uj;
    }

    //Idsi verilen işe bağlı bütün kullanıcıları getirir
    public List<Users_Jobs> isdenBul(int id){
        List<Users_Jobs> uj = new ArrayList<>();
        repository.getByJob(id).forEach(uj:: add);
        return uj;
    }

    //Idsi verilen işe bağlı kullanıcıların sayısını döner
    public int isdekiKullanicilar(int id){
        List<Users_Jobs> uj = new ArrayList<>();
        repository.getByJob(id).forEach(uj:: add);
        return uj.size();
    }

    //Yeni kullanıcı-iş ikilisi ekler
    public boolean kullaniciIsEkle(Users_Jobs uj){
        List<Users_Jobs> list = new ArrayList<>();
        repository.getByUser(uj.getUser_id()).forEach(list:: add);
        for(int i = 0; i<list.size(); i++){
            Users_Jobs temp = list.get(i);
            if(temp.getJob_id() == uj.getJob_id()){
                return false;
            }
        }
        repository.insertJob(uj.getUser_id(), uj.getJob_id());
        return true;
    }

    //Kullanıcı-iş ikilisini siler(Kullanıcının iş listesinden işi kaldırır)
    public boolean kullaniciIsiSil(Users_Jobs uj){
        repository.delete(uj);
        Messages message = new Messages("::RED::Görev bırakıldı.", uj.getUser_id());
        m_service.mesajEkle(message, uj.getUser_id(), uj.getJob_id());
        return true;
    }
}
