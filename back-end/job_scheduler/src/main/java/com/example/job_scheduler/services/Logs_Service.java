package com.example.job_scheduler.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.entities.Logs;
import com.example.job_scheduler.repositories.Logs_Repository;

@Service
public class Logs_Service {
    @Autowired
    private Logs_Repository repository;

    //Bütün logları listeler
    public List<Logs> butunLoglar(){
        List<Logs> list = new ArrayList<>();
        repository.all().forEach(list::add);
        return list;
    }
    
    //Bu ayın loglarını listeler
    public List<Logs> buAy(String month){
        List<Logs> list = new ArrayList<>();
        repository.getThisMonth(month).forEach(list::add);
        return list;
    }

    //Bu yılın loglarını listeler
    public List<Logs> buYil(String year){
        List<Logs> list = new ArrayList<>();
        repository.getThisYear(year).forEach(list::add);
        return list;
    }
    
    //Bu günün loglarını listeler
    public List<Logs> bugun(String day){
        List<Logs> list = new ArrayList<>();
        repository.getToday(day).forEach(list::add);
        return list;
    }

    //Verilen tarihler arasındaki log kayıtlarını listeler
    public List<Logs> tarihlerArasi(String day1, String day2){
        List<Logs> list = new ArrayList<>();
        repository.getBetween(day1, day2).forEach(list::add);
        return list;
    }
    
    //Verilen kullanıcının loglarını listeler
    public List<Logs> kullaniciLogu(String e_posta){
        List<Logs> list = new ArrayList<>();
        repository.getByUser(e_posta).forEach(list::add);
        return list;
    }

    //Yeni log ekler
    public boolean logEkle(Logs log){
        repository.insertJob(log.getLog_record(), log.getE_posta(), log.getDate_time());
        return true;
    }
}
