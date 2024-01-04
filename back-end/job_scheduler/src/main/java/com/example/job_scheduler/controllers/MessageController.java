package com.example.job_scheduler.controllers;

import com.example.job_scheduler.model.Response;
import com.example.job_scheduler.entities.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.job_scheduler.Config;
import com.example.job_scheduler.entities.Jobs;
import com.example.job_scheduler.entities.Logs;
import com.example.job_scheduler.entities.Messages;
import com.example.job_scheduler.services.Messages_Service;
import com.example.job_scheduler.entities.Messages_Jobs;
import com.example.job_scheduler.services.Jobs_Service;
import com.example.job_scheduler.services.Logs_Service;
import com.example.job_scheduler.services.Messages_Jobs_Service;
import com.example.job_scheduler.services.Messages_Users_Service;
import com.example.job_scheduler.services.Users_Service;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    Messages_Service m_service;
    @Autowired
    Messages_Jobs_Service mj_service;
    @Autowired
    Messages_Users_Service mu_service;
    @Autowired
    Jobs_Service js;
    @Autowired
    Users_Service us;
    @Autowired
    Logs_Service ls;

    //Idsi verilen işe kayıtlı mesajları listeler
    @GetMapping("/{id}")
    public ResponseEntity<Response> getJobsMessages(@PathVariable int id){
        Map<Integer, Messages> map = new HashMap<Integer, Messages>();
        List<Messages_Jobs> mj = mj_service.isdenBul(id);
        for(int i = 0; i<mj.size(); i++){
            map.put(mj.size()-i, m_service.mesajiBul(mj.get(i).getMesaj_id()));
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("İşe kayıtlı mesajlar")
            .data(map)
            .build()
            );
    }

    //Idsi veirlen işe yeni mesaj ekler
    @PostMapping("/{id}/insert")
    public ResponseEntity<Response> insertNewMessage(HttpServletRequest request, @RequestParam String message, @PathVariable int id, @RequestParam String e_mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(e_mail);
        if(!(username.equals(user.getE_posta()))){
            message = username+" as "+e_mail+" "+message;
            user = us.kullaniciyiBul(username);
        }
        Jobs job = js.isiGetir(id);
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(id+"Idsine sahip ve "+job.getIsim()+" isimli işe yeni mesaj ekledi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        Messages m = new Messages(message, user.getId());
        m_service.mesajEkle(m, user.getId(), id);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Mesaj eklendi")
            .build()
            );
    }
}
