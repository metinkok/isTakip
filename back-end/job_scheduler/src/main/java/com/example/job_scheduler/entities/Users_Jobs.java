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
public class Users_Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int user_id;
    private int job_id;

    public Users_Jobs(){
        id = -1;
        user_id = -1;
        job_id = -1;
    }
    public Users_Jobs(int id, int user_id, int job_id){
        this.id = id;
        this.user_id = user_id;
        this.job_id = job_id;
    }
    public Users_Jobs(int user_id, int job_id){
        id = -1;
        this.user_id = user_id;
        this.job_id = job_id;
    }
}
