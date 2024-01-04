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
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String mesaj;
    private int user_id;

    public Messages(){
        id = -1;
        mesaj = null;
        user_id = -1;
    }
    public Messages(int id, String mesaj, int user_id){
        this.id = id;
        this.mesaj = mesaj;
        this.user_id = user_id;
    }
    public Messages(String mesaj, int user_id){
        id = -1;
        this.mesaj = mesaj;
        this.user_id = user_id;
    }
}
