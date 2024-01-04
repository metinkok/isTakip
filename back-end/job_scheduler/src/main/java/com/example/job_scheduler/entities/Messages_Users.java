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
public class Messages_Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int mesaj_id;
    private int user_id;
    public Messages_Users(){
        id = -1;
        mesaj_id = -1;
        user_id = -1;
    }
    public Messages_Users(int id, int mesaj_id, int user_id){
        this.id = id;
        this.mesaj_id = mesaj_id;
        this.user_id = user_id;
    }
    public Messages_Users(int mesaj_id, int user_id){
        id = -1;
        this.mesaj_id = mesaj_id;
        this.user_id = user_id;
    }
}

