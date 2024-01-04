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
public class Messages_Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int mesaj_id;
    private int job_id;

    public Messages_Jobs(){
        id = -1;
        mesaj_id = -1;
        job_id = -1;
    }
    public Messages_Jobs(int id, int mesaj_id, int job_id){
        this.id = id;
        this.mesaj_id = mesaj_id;
        this.job_id = job_id;
    }
    public Messages_Jobs(int mesaj_id, int job_id){
        id = -1;
        this.mesaj_id = mesaj_id;
        this.job_id = job_id;
    }
}

