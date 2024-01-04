package com.example.job_scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.job_scheduler.entities.Logs;
import com.example.job_scheduler.services.Logs_Service;
import com.example.job_scheduler.model.Response;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.job_scheduler.Config;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/logs")
public class LogsController {
    @Autowired
    private Logs_Service ls;
    
    //Bütün log kayıtlarını listeler
    @GetMapping("/")
    public ResponseEntity<Response> getAllLogs(HttpServletRequest request){
        List<Logs> list = ls.butunLoglar();
        Map<Integer, Logs> map = new HashMap<Integer, Logs>();
        for(int i=0; i<list.size(); i++){
            map.put(i, list.get(i));
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün loglar")
            .data(map)
            .build()
            );
    }

    //Bugünün log kayıtlarını listeler
    @PostMapping("/today")
    public ResponseEntity<Response> getTodayLogs(HttpServletRequest request, @RequestParam String today){
        /*String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();*/
        List<Logs> list = ls.bugun(today);
        Map<Integer, Logs> map = new HashMap<Integer, Logs>();
        for(int i=0; i<list.size(); i++){
            map.put(i, list.get(i));
        }
      /*Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Log kayıtları görüntülendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);*/
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bugünün log kayıtları")
            .data(map)
            .build()
            );
    }

    //Bu ayın log kayıtlarını listeler
    @PostMapping("/month")
    public ResponseEntity<Response> getMonthLogs(HttpServletRequest request, @RequestParam String month){
        List<Logs> list = ls.buAy(month);
        Map<Integer, Logs> map = new HashMap<Integer, Logs>();
        for(int i=0; i<list.size(); i++){
            map.put(i, list.get(i));
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bu ayın log kayıtları")
            .data(map)
            .build()
            );
    }

     //Bu yılın log kayıtlarını listeler
    @PostMapping("/year")
    public ResponseEntity<Response> getYearLogs(HttpServletRequest request, @RequestParam String year){
        List<Logs> list = ls.buYil(year);
        Map<Integer, Logs> map = new HashMap<Integer, Logs>();
        for(int i=0; i<list.size(); i++){
            map.put(i, list.get(i));
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bu yılın log kayıtları")
            .data(map)
            .build()
            );
    }

    //Verilen tarihler arasındaki log kayıtlarını listeler
    @PostMapping("/between")
    public ResponseEntity<Response> getYearLogs(HttpServletRequest request, @RequestParam String day1, @RequestParam String day2){
        List<Logs> list = ls.tarihlerArasi(day1, day2);
        Map<Integer, Logs> map = new HashMap<Integer, Logs>();
        for(int i=0; i<list.size(); i++){
            map.put(i, list.get(i));
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Verilen tarihler arasındaki log kayıtları")
            .data(map)
            .build()
            );
    }

    //Yeni log  kaydı ekler
    @PostMapping("/insert")
    public ResponseEntity<Response> insertLog(HttpServletRequest request, @RequestParam String message){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();

        Logs log = new Logs();
        log.setE_posta(username);
        log.setDate_time(""+LocalDateTime.now());
        log.setLog_record(message);
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Yeni log kaydı oluşturuldu.")
            .build()
            );
    }
}