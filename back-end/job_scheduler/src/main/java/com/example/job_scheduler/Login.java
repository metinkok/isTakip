package com.example.job_scheduler;

import java.io.Serializable;

//Login için obje
public class Login implements Serializable{
    private String userName;
    private String password;
    private int admin;
    
    public Login(String userName, String password, int admin){
        this.userName = userName;
        this.password = password;
        this.admin = admin;
    }
    public Login(){
        userName="";
        password="";
        admin = 0;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setAdmin(int admin){
        this.admin = admin;
    }
    
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public int getAdmin(){
        return admin;
    }
}
