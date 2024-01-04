package com.example.job_scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.job_scheduler.Config;
import com.example.job_scheduler.entities.Jobs;
import com.example.job_scheduler.entities.Logs;
import com.example.job_scheduler.entities.Messages;
import com.example.job_scheduler.entities.Users;
import com.example.job_scheduler.entities.Users_Jobs;
import com.example.job_scheduler.services.Jobs_Service;
import com.example.job_scheduler.services.Logs_Service;
import com.example.job_scheduler.services.Messages_Service;
import com.example.job_scheduler.services.Users_Jobs_Service;
import com.example.job_scheduler.services.Users_Service;
import com.example.job_scheduler.model.Response;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/jobs")
public class JobsController {
    @Autowired
    private Jobs_Service js;
    @Autowired
    private Users_Jobs_Service ujs;
    @Autowired
    private Messages_Service m_service;
    @Autowired
    private Users_Service us;
    @Autowired
    private Logs_Service ls;
    
    //Tüm aktif işleri listeler
    @GetMapping("/")
    public ResponseEntity<Response> getAllActiveJobs(HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.butunAktifIsler();
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getId());
            if(job.getStatus() != 3)
                jobs.add(job);
            else
                jobs.add(0, job);
        }
        for(int i = 0; i<jobs.size(); i++){
            map.put(jobs.size()-i, jobs.get(i));
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Bütün işler listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün işler")
            .data(map)
            .build()
            );
    }

    //Tüm işleri listeler
    @GetMapping("/inactive")
    public ResponseEntity<Response> getAllJobs(HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.butunInaktifIsler();
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getId());
            if(job.getStatus() != 3)
                jobs.add(job);
            else
                jobs.add(0, job);
        }
        for(int i = 0; i<jobs.size(); i++){
            map.put(jobs.size()-i, jobs.get(i));
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Bütün inaktif işler listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün inaktif işler")
            .data(map)
            .build()
            );
    }

    //Idsi verilen işin alt işlerini listeler
    @PostMapping("/subjobs")
    public ResponseEntity<Response> getSubJobs(HttpServletRequest request, @RequestParam int parent){
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.altIsleriGetir(parent);
        for(int i = 0; i<list.size(); i++){
            map.put(list.size()-i, list.get(i));
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün alt işler")
            .data(map)
            .build()
            );
    }

    //Statüsü verilen statüdeki işleri listeler
    @GetMapping("/status/{status}")
    public ResponseEntity<Response> getAllJobs(HttpServletRequest request, @PathVariable int status){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.statusundekiIsler(status);
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getId());
            if(job.getStatus() != 3)
                jobs.add(job);
            else
                jobs.add(0, job);
        }
        for(int i = 0; i<jobs.size(); i++){
            map.put(jobs.size()-i, jobs.get(i));
        }
        String stts = "Aktif iş";
        if(status == 2)
            stts = "Üzerinde çalışılan iş";
        else if(status == 3)
            stts = "Bitmiş iş";
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Bütün "+stts+" statüsündeki işler listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün işler")
            .data(map)
            .build()
            );
    }
    //Deadline'ı bugün olan işleri listeler
    @GetMapping("/today/{today}")
    public ResponseEntity<Response> getToday(HttpServletRequest request, @PathVariable String today){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.bugununIsleri(today);
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getId());
            if(job.getStatus() != 3)
                jobs.add(job);
            else
                jobs.add(0, job);
            }
        for(int i = 0; i<jobs.size(); i++){
            map.put(jobs.size()-i, jobs.get(i));
            }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Deadline'ı bugün biten işler listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün işler")
            .data(map)
            .build()
            );
    }
    //Deadline'ı bugün olan işleri listeler
    @GetMapping("/deadline/{deadline}")
    public ResponseEntity<Response> getNotEnded(HttpServletRequest request, @PathVariable String deadline){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.deadllineGecmemis(deadline);
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getId());
            if(job.getStatus() != 3)
                jobs.add(job);
            else
                jobs.add(0, job);
            }
        for(int i = 0; i<jobs.size(); i++){
            map.put(jobs.size()-i, jobs.get(i));
            }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Deadline'ı geçmemiş bütün işler listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün işler")
            .data(map)
            .build()
            );
    }

     //Deadline'ı verilen deadline'ın ayında olan işleri listeler olan işleri listeler
    @GetMapping("/deadline/month/{deadline}")
    public ResponseEntity<Response> getMonth(HttpServletRequest request, @PathVariable String deadline){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        deadline = deadline.substring(0, deadline.length()-2);
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.deadlineAy(deadline);
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getId());
            if(job.getStatus() != 3)
                jobs.add(job);
            else                        
                jobs.add(0, job);
            }
        for(int i = 0; i<jobs.size(); i++){
            map.put(jobs.size()-i, jobs.get(i));
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Deadline'ı bu ay olan bütün işler listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün işler")
            .data(map)
            .build()
            );
    }

    //İsmi içerisinde verilen string geçen işleri listeler
    @PostMapping("/search/name")
    public ResponseEntity<Response> ara(HttpServletRequest request, @RequestParam String name){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Jobs> list = js.isimleAra(name);
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getId());
            if(job.getStatus() != 3)
                jobs.add(job);
            else
                jobs.add(0, job);
            }
            for(int i = 0; i<jobs.size(); i++){
                map.put(jobs.size()-i, jobs.get(i));
            }
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("İsmi içerisinde "+name+" geçen bütün işler listelendi.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("İsmi içerisinde "+name+" geçen işler")
                .data(map)
                .build()
                );
        }
    
    //Idsi adreste olan işin bilgilerini gösterir
    @GetMapping("/{id}")
    public ResponseEntity<Response> getJob(HttpServletRequest request, @PathVariable int id){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Jobs job = js.isiGetir(id);
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        map.put(1, job);
        if(job != null){
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Idsi "+id+" ve ismi "+job.getIsim()+" olan iş görüntülendi.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("İstek başarılı")
                .data(map)
                .build()
                );
        }
        Logs log = new Logs();
        log.setE_posta(username);
        if(id == -1)
            log.setLog_record("Hatalı istek listelenecek iş bulunamadı.");
        else
            log.setLog_record("Hatalı istek "+id+"idsine sahip iş bulunamadı.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(BAD_REQUEST)
            .message("8492")
            .build()
            );
    }

    //Yeni iş ekler
    @PostMapping("/insert")
    public ResponseEntity<Response> insertJob(HttpServletRequest request, @RequestParam String name, @RequestParam String expl, @RequestParam int status, @RequestParam int point, @RequestParam String deadline){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Jobs job = new Jobs(name, expl, point, status);
        job.setDeadline(deadline);
        if(status == 3){
            if(js.isEkle(job)){
                Logs log = new Logs();
                log.setE_posta(username);
                log.setLog_record("Yeni iş eklendi isim: "+job.getIsim());
                log.setDate_time(""+LocalDateTime.now());
                ls.logEkle(log);
                return ResponseEntity.ok(
                    Response.builder()
                    .time(now())
                    .httpStatus(I_AM_A_TEAPOT)
                    .message("Tamamlanmış iş eklendi")
                    .build()
                    );
            }
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Başarısız iş ekleme isteği.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(INTERNAL_SERVER_ERROR)
                .message("Bir hata ile karşılaşıldı")
                .build()
                );
        }
        else{
        if(js.isEkle(job)){
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Yeni iş eklendi isim: "+job.getIsim());
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("İş eklendi")
                .build()
                );
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Başarısız iş ekleme isteği.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
           .time(now())
           .httpStatus(INTERNAL_SERVER_ERROR)
           .message("Bir hata ile karşılaşıldı")
           .build()
            );
        }

    }

    //Yeni alt iş ekler
    @PostMapping("/subjob/insert")
    public ResponseEntity<Response> insertSubJob(HttpServletRequest request, @RequestParam String name, @RequestParam String expl, @RequestParam int status, @RequestParam int point, @RequestParam String deadline, @RequestParam int parent){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Jobs job = new Jobs(name, expl, point, status);
        job.setDeadline(deadline);
        job.setParent(parent);
        if(status == 3){
            if(js.altIsEkle(job)){
                Logs log = new Logs();
                log.setE_posta(username);
                log.setLog_record("Yeni alt iş eklendi isim: "+job.getIsim());
                log.setDate_time(""+LocalDateTime.now());
                ls.logEkle(log);
                return ResponseEntity.ok(
                    Response.builder()
                    .time(now())
                    .httpStatus(I_AM_A_TEAPOT)
                    .message("Tamamlanmış alt iş eklendi")
                    .build()
                    );
            }
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Başarısız alt iş ekleme isteği.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(INTERNAL_SERVER_ERROR)
                .message("Bir hata ile karşılaşıldı")
                .build()
                );
        }
        else{
        if(js.altIsEkle(job)){
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Yeni alt iş eklendi isim: "+job.getIsim());
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("İş eklendi")
                .build()
                );
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Başarısız alt iş ekleme isteği.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
           .time(now())
           .httpStatus(INTERNAL_SERVER_ERROR)
           .message("Bir hata ile karşılaşıldı")
           .build()
            );
        }
    }

    //Idsi verilen işin statüsünü günceller
    @PostMapping("/{id}/{status}")
    public ResponseEntity<Response> setStatus(HttpServletRequest request, @PathVariable int id, @PathVariable int status){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Jobs job = js.isiGetir(id);
        js.statuGuncelle(status, id);
        String active="Aktif";
        if(status == 3)
            active = "Tamamlandı";
        else if(status == 2)
            active = "Üzerinde çalışılıyor";
        else if(status == 4)
            active = "İnaktif";
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(job.getIsim()+" isimli işin statüsü "+active+" olarak güncellendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message(job.getIsim()+" isimli işin statüsü "+active+" olarak güncellendi.")
            .build()
            );
    }

    //Idsi verilen işin statüsünü günceller
    @PostMapping("/deadline/update")
    public ResponseEntity<Response> setDeadline(HttpServletRequest request, @RequestParam int id, @RequestParam String deadline){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(username);
        Jobs job = js.isiGetir(id);
        js.deadlineGuncelle(deadline, id);
        Messages message = new Messages("::YELLOW::İşin deadline'ı "+deadline+" olarak güncellendi.", user.getId());
        m_service.mesajEkle(message, user.getId(), id);
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(job.getIsim()+" isimli işin deadline'ı "+deadline+" olarak güncellendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message(job.getIsim()+" isimli işin deadline'ı "+deadline+" olarak güncellendi.")
            .build()
            );
        }

    //Aktif kullanıcıya işi atar
    @PostMapping("/{id}/take")
    public ResponseEntity<Response> takeJob(HttpServletRequest request, @RequestParam String e_mail, @PathVariable int id){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(username);
        Users_Jobs uj = new Users_Jobs(user.getId(), id);
        if(ujs.kullaniciIsEkle(uj)){
            Messages message = new Messages("::GREEN::İşi "+user.getE_posta()+" kullanıcısı almıştır.", user.getId());
            m_service.mesajEkle(message, user.getId(), id);
            Jobs job = js.isiGetir(id);
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record(id+"Idli ve "+job.getIsim()+" isimli iş alındı.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("İş alındı.")
                .build()
                );
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(id+"Idli iş alınamadı.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("İş zaten üzerinizde")
            .build()
            );
    }

    //Idsi verilen işi idsi gönderilen kullanıcıya atar
    @PostMapping("/{job_id}/give")
    public ResponseEntity<Response> giveJob(HttpServletRequest request, @PathVariable int job_id, @RequestParam int user_id, @RequestParam String e_mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(username);
        Users_Jobs uj = new Users_Jobs(user_id, job_id);
        Users atanan = us.kullaniciyiBul(user_id);
        if(ujs.kullaniciIsEkle(uj)){
            Messages message = new Messages("::BLUE::İş "+atanan.getE_posta()+" kullanıcısına atanmıştır.", user.getId());
            m_service.mesajEkle(message, user.getId(), job_id);
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record(job_id+" Idsine sahip iş "+atanan.getE_posta()+" kullanıcısına atanmıştır.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("İş ataması başarılı.")
                .build()
                );
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Hatalı işlem: "+job_id+" Idsine sahip iş "+atanan.getE_posta()+" kullanıcısına atanamamıştır.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("İş zaten kullanıcının üzerinde")
            .build()
            );
    }
    
    //Üzerindeki işi bırakma
    @PostMapping("/{job_id}/withdraw")
    public ResponseEntity<Response> withdrawJob(HttpServletRequest request, @PathVariable int job_id, @RequestParam String e_mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(username);
        List<Users_Jobs> list = ujs.isdenBul(job_id);
        Jobs job = js.isiGetir(job_id);
        for(int i = 0; i< list.size(); i++){
            if(list.get(i).getUser_id() == user.getId()){
                ujs.kullaniciIsiSil(list.get(i));
                Logs log = new Logs();
                log.setE_posta(username);
                log.setLog_record(job_id+" Idsine sahip "+job.getIsim()+" işini"+e_mail+" kullanıcısı bırakmıştır.");
                log.setDate_time(""+LocalDateTime.now());
                ls.logEkle(log);
                return ResponseEntity.ok(
                    Response.builder()
                    .time(now())
                    .httpStatus(OK)
                    .message("Görev bırakıldı")
                    .build()
                    );
            }
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Hatalı işlem:"+job_id+" Idsine sahip "+job.getIsim()+" işini"+e_mail+" kullanıcısı bırakamamıştır.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(BAD_REQUEST)
                .message("İş zaten kullanıcının üzeirnde değil")
                .build()
                );
    }

    //Idsi verilen işi üzerinde tutan kullanıcıları listeler
    @GetMapping("/{job_id}/users")
    public ResponseEntity<Response> usersInJob(HttpServletRequest request, @PathVariable int job_id){
        List<Users_Jobs> list = ujs.isdenBul(job_id);
        Map<Integer, Users> map = new HashMap<Integer, Users>();
        for(int i = 0; i< list.size(); i++){
            Users user = us.kullaniciyiBul(list.get(i).getUser_id());
            user.setAdmn(-1);
            user.setSifre("");
            map.put(i, user);
        }
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("Kullanıcılar bulundu.")
                .data(map)
                .build()
                );
    }

    //Idsi verilen işin puanını günceller
    @PostMapping("/{job_id}/point/update")
    public ResponseEntity<Response> updatePoint(HttpServletRequest request, @PathVariable int job_id, @RequestParam int point){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Jobs job = js.isiGetir(job_id);
        if(job == null){
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("İş bulunamadı.")
                .build()
                );
        }
        js.puanGuncelle(point, job_id);
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(job_id+" idsine sahip "+job.getIsim()+" iişinin puanı güncellenmiştir.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("Puan güncellendi.")
                .build()
                );
    }
}