package com.example.job_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String log_record;
    private String e_posta;
    private String date_time;

    public Logs(){
        id = -1;
        log_record = "";
        e_posta = "";
        date_time = "";
    }
    public Logs(int id, String log_record, String e_posta, String date_time){
        this.id = id;
        this. log_record = log_record;
        this.e_posta = e_posta;
        this.date_time = date_time;
    }
}