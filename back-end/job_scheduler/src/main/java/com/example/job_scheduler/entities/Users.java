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
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String isim;
    private String e_posta;
    private String sifre;
    private int point;
    private int admn;//1 admin diğerleri değil ileride hiyerarşi için modifiye edilebilir
    private int active;

    public Users(){
        id = -1;
        isim = null;
        e_posta = null;
        sifre = null;
        point = -1;
        admn = -1;
        active = -1;
    }
    public Users(int id, String isim, String e_posta, String sifre, int point, int admn, int active){
        this.id = id;
        this.isim = isim;
        this.e_posta = e_posta;
        this.sifre = sifre;
        this.point = point;
        this.admn = admn;
        this.active = active;
    }
    public Users(String isim, String e_posta, String sifre){
        id = -1;
        this.isim = isim;
        this.e_posta = e_posta;
        this.sifre = sifre;
        point = -1;
        admn = -1;
        active = -1;
    }
    public Users(int id){
        this.id = id;
        isim = null;
        e_posta = null;
        sifre = null;
        point = -1;
        admn = -1;
        active = -1;
    }
}
