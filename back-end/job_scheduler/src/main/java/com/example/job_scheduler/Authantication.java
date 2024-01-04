package com.example.job_scheduler;

import com.example.job_scheduler.controllers.MainCantroller;
import com.example.job_scheduler.model.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class Authantication implements AuthenticationManager{
    @Autowired
    private MainCantroller main_controller;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Login login = new Login();
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        login.setPassword(password);
        login.setUserName(username);

        if(username != null){
            Response respone = main_controller.loginPage(login).getBody();
            if(respone != null)
                if(respone.getMessage().equals("Oturum açma işlemi başarılı")){
                    return authentication;
                }
            authentication.setAuthenticated(false);
        }
        return null;
    }

}