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
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String isim;
    private String aciklama;
    private int status;//1 active 2 working 3 closed
    private int point;
    private Integer parent;
    private String deadline;

    public Jobs(){
        id = -1;
        isim = null;
        aciklama = null;
        point = -1;
        status = -1;
        parent = null;
        deadline = null;
    }
    public Jobs(int id, String isim, String aciklama, int point, int status, int parent, String deadline){
        this.id = id;
        this.isim = isim;
        this.aciklama = aciklama;
        this.point = point;
        this.status = status;
        this.parent = parent;
        this.deadline = deadline;
    }
    public Jobs(String isim, String aciklama, int point, int status){
        this.id = -1;
        this.isim = isim;
        this.aciklama = aciklama;
        this.point = point;
        this.status = status;
        parent = null;
        deadline = null;
    }
}
