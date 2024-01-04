package com.example.job_scheduler.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_scheduler.entities.Jobs;
import com.example.job_scheduler.repositories.Jobs_Repository;

@Service
public class Jobs_Service {
    @Autowired
    private Jobs_Repository repository;

    //Bütün aktif işleri listeler
    public List<Jobs> butunAktifIsler(){
        List<Jobs> list = new ArrayList<>();
        repository.allActive().forEach(list::add);
        return list;
    }

    //Bütün inaktif işleri listeler
    public List<Jobs> butunInaktifIsler(){
        List<Jobs> list = new ArrayList<>();
        repository.allInactive().forEach(list::add);
        return list;
    }
    
    //Verilen statüdeki işleri listeler
    public List<Jobs> statusundekiIsler(int status){
            List<Jobs> list = new ArrayList<>();
            repository.getWithStatus(status).forEach(list::add);
            return list;
    }

    //Bugünün işlerini listeler
    public List<Jobs> bugununIsleri(String today){
        List<Jobs> list = new ArrayList<>();
        repository.getToday(today).forEach(list::add);
        return list;
    }
    //Deadline'ı geçmemiş işleri listeler
    public List<Jobs> deadllineGecmemis(String deadline){
        List<Jobs> list = new ArrayList<>();
        repository.getDeadline(deadline).forEach(list::add);
        return list;
    }

    //Deadline'ı verilen ay olan işleri listeler
    public List<Jobs> deadlineAy(String deadline){
        List<Jobs> list = new ArrayList<>();
        repository.getDeadlineMonth(deadline).forEach(list::add);
        return list;
    }

    //İsmi içerisinde verilen string geçen işleri listeler
    public List<Jobs> isimleAra(String isim){
        List<Jobs> list = new ArrayList<>();
        repository.searchLike(isim).forEach(list::add);
        return list;
    }

    //Idsi verilen işin statüsünü verilen statüye günceller (1 aktif, 2 üzerinde çalışılıyor, 3 tamamlandı)
    public boolean statuGuncelle(int status, int id){
        repository.updateStatus(status, id);
        return true;
    }

    //Idsi verilen işi getirir
    public Jobs isiGetir(int id){
        Jobs job = repository.getById(id);
        return job;
    }

    //Idsi verilen işin alt işlerini getirir
    public List<Jobs> altIsleriGetir(int id){
        List<Jobs> list = new ArrayList<>();
        repository.subJobs(id).forEach(list::add);
        return list;
    }

    //Yeni iş ekler
    public boolean isEkle(Jobs job){
        repository.insertJob(job.getIsim(), job.getAciklama(), job.getStatus(), job.getPoint(), job.getDeadline());
        return true;
    }

    //Yeni alt iş ekler
    public boolean altIsEkle(Jobs job){
        repository.insertSubJob(job.getIsim(), job.getAciklama(), job.getStatus(), job.getPoint(), job.getDeadline(), job.getParent());
        return true;
    }

    //Idsi verilen işin deadlineını günceller
    public boolean deadlineGuncelle(String deadline, int id){
        repository.updateDeadline(deadline, id);
        return true;
    }

    //Idsi verilen işin puanını günceller
    public boolean puanGuncelle(int puan, int id){
        repository.updatePoint(puan, id);
        return true;
    }
}
