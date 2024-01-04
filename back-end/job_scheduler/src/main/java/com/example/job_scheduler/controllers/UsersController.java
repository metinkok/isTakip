package com.example.job_scheduler.controllers;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.job_scheduler.Config;
import com.example.job_scheduler.Sha256;
import com.example.job_scheduler.entities.Jobs;
import com.example.job_scheduler.entities.Logs;
import com.example.job_scheduler.entities.Users;
import com.example.job_scheduler.entities.Users_Jobs;
import com.example.job_scheduler.model.Response;
import com.example.job_scheduler.services.Jobs_Service;
import com.example.job_scheduler.services.Logs_Service;
import com.example.job_scheduler.services.Users_Jobs_Service;
import com.example.job_scheduler.services.Users_Service;
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    Users_Service us;
    @Autowired
    Jobs_Service js;
    @Autowired
    Users_Jobs_Service ujs;
    @Autowired
    Logs_Service ls;

    //Bütün kullanıcıları listeler
    @GetMapping("/")
    public ResponseEntity<Response> getAllUsers(HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Users> map = new HashMap<Integer, Users>();
        List<Users> users = us.butunKullanicilar();
        for(int i = 0; i<users.size(); i++){
            Users user = new Users();
            user.setId(users.get(i).getId());
            user.setE_posta(users.get(i).getE_posta());
            user.setIsim(users.get(i).getIsim());
            user.setPoint(users.get(i).getPoint());
            map.put(i, user);
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Bütün kullanıcılar listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün kullanıcılar")
            .data(map)
            .build()
            );
    }
    //Bütün kullanıcıları listeler
    @GetMapping("/admin")
    public ResponseEntity<Response> getAllUsersWithAdmn(HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Users> map = new HashMap<Integer, Users>();
        List<Users> users = us.butunKullanicilarAdmin();
        for(int i = 0; i<users.size(); i++){
            Users user = new Users();
            user.setId(users.get(i).getId());
            user.setE_posta(users.get(i).getE_posta());
            user.setIsim(users.get(i).getIsim());
            user.setAdmn(users.get(i).getAdmn());
            user.setPoint(users.get(i).getPoint());
            user.setActive(users.get(i).getActive());
            map.put(i, user);
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Bütün kullanıcılar listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Bütün kullanıcılar")
            .data(map)
            .build()
            );
    }

    //İsminde verilen string geçen kullanıcıları listeler
    @PostMapping("/search")
    public ResponseEntity<Response> ara(HttpServletRequest request, @RequestParam String name){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Map<Integer, Users> map = new HashMap<Integer, Users>();
        List<Users> users = us.isimleAra(name);
        for(int i = 0; i<users.size(); i++){
            users.get(i).setSifre("");
            users.get(i).setAdmn(-1);
            map.put(i, users.get(i));
        }
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("İsminde "+name+" geçen kullanıcılar listelendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("İsminde "+name+" geçen kullanıcılar")
            .data(map)
            .build()
            );
    }

    //Idsi verilen kullanıcıyı döner
    @PostMapping("/")
    public ResponseEntity<Response> kullaniciBul(HttpServletRequest request, @RequestParam int id){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(id);
        if(user == null){
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Hatalı istek: "+id+" Idsine sahip kullanıcı bulunamadı.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("Kullanıcı bulunamadı")
                .build()
                );
        }
        user.setSifre("");
        user.setAdmn(-1);
        Map<Integer, Users> map = new HashMap<Integer, Users>();
        map.put(0, user);
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("Kullanıcı bulundu")
                .data(map)
                .build()
                );
    }
    
    //e_maili verilen kullanıcının işlerini listeler
    @GetMapping("/{e_mail}")
    public ResponseEntity<Response> showProfile(@PathVariable String e_mail, HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(e_mail);
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Users_Jobs> list = ujs.KullanicidanBul(user.getId());
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getJob_id());
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
        log.setLog_record(e_mail+" e-posta adresine sahip kullanıcı görüntülendi.");
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

    //e-maili verilen kullanıcının üzerindeki verilen statüdeki işleri listeler
    @GetMapping("/{e_mail}/{status}")
    public ResponseEntity<Response> showProfileFilter(@PathVariable String e_mail, @PathVariable int status, HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(e_mail);
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Users_Jobs> list = ujs.KullanicidanBul(user.getId());
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getJob_id());
            if(job.getStatus() == status)
                jobs.add(job);
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
        log.setLog_record(e_mail+" e-posta adresine sahip kullanıcının "+stts+" statüsündeki işleri görüntülendi.");
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

    //e-maili verilen kullanıcının üzerindeki verilen deadline içerisinde bulunduğu ayın işlerini listeler
    @GetMapping("/{e_mail}/deadline/{deadline}")
    public ResponseEntity<Response> showProfileFilterMonth(@PathVariable String e_mail, @PathVariable String deadline, HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(e_mail);
        if(user == null){
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Hatalı istek listelenecek kullanıcı bulunamadı.");
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
        deadline = deadline.substring(0, deadline.length()-2);
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Users_Jobs> list = ujs.KullanicidanBul(user.getId());
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getJob_id());
            if(job.getDeadline().contains(deadline))
                jobs.add(job);
        }
        for(int i = 0; i<jobs.size(); i++){
            map.put(jobs.size()-i, jobs.get(i));
        }
        deadline = deadline.substring(0, deadline.length()-1);
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(e_mail+" e-posta adresine sahip kullanıcının "+deadline+" ayındaki işleri görüntülendi.");
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

    //e_maili verilen adminin işlerini listeler
    @GetMapping("/{e_mail}/admin")
    public ResponseEntity<Response> showAdminProfile(@PathVariable String e_mail, HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(e_mail);
        Map<Integer, Jobs> map = new HashMap<Integer, Jobs>();
        List<Users_Jobs> list = ujs.KullanicidanBul(user.getId());
        List<Jobs> jobs = new ArrayList<Jobs>();
        for(int i=0; i<list.size(); i++){
            Jobs job = js.isiGetir(list.get(i).getJob_id());
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
        log.setLog_record(e_mail+" e-postasına sahip kullanıcı görüntülendi.");
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

    //e_maili verilen kullanıcıya adminlik yetkisi verir yetkinin değişikliğin kullanılabilmesi için Idsi verilen kullanıcının tekrar giriş yapması gerekir
    @PostMapping("/{e_mail}/authorization")
    public ResponseEntity<Response> yetkiDegistir(HttpServletRequest request, @PathVariable String e_mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users u = us.kullaniciyiBul(e_mail);
        us.adminYap(u.getId());
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(e_mail+" e-postasına sahip kullanıcıya admin yetkisi verildi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message(e_mail+" kullanıcısı artık admin yetkisine sahip")
                .build()
                );
    }

    //e_maili verilen kullanıcının admin yetkisini kaldırır değişikliğin kullanılabilmesi için Idsi verilen kullanıcının tekrar giriş yapması gerekir
    @PostMapping("/{e_mail}/authorization/revoke")
    public ResponseEntity<Response> yetkiyiAl(HttpServletRequest request, @PathVariable String e_mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users u = us.kullaniciyiBul(e_mail);
        us.adminlikAl(u.getId());
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(e_mail+" e-postasına ship kullanıcıdan admin yetkisi alındı.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message(e_mail+" kullanıcısı artık admin yetkisine sahip değil")
                .build()
                );
    }

    //e_maili verilen kullanıcın şifresini sıfırlar kullanıcının e_mailinin verilen e_maille aynı olması gerekir
    @PostMapping("/{e_mail}/reset_password")
    public ResponseEntity<Response> sifreSifirla(HttpServletRequest request, @PathVariable String e_mail, @RequestParam String password, @RequestParam String mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(mail);
        Users u = us.kullaniciyiBul(e_mail);
        if(u.getId() != user.getId()){//Kullanıcı kendi şifresini değiştirebilir
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(FORBIDDEN)
                .message("Bu işlem için yetkiniz bulunmamaktadır")
                .build()
                );
        }
        Sha256 sha256 = new Sha256();
        password = sha256.hash(password);
        us.sifreDegistir(u.getId(), password);
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(e_mail+" e-postasına sahip kullanıcının şifresi değiştirildi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message(e_mail+" kullanıcısının şifresi güncellendi")
                .build()
                );
    }

    //e_maili verilen kullanıcın şifresini sıfırlar
    @PostMapping("/{e_mail}/reset_password/admin")
    public ResponseEntity<Response> sifreSifirlaAdmin(HttpServletRequest request, @PathVariable String e_mail, @RequestParam String password, @RequestParam String mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
        Users user = us.kullaniciyiBul(mail);
        Users u = us.kullaniciyiBul(e_mail);
        if(user.getAdmn() != 1){//Kullanıcı kendi şifresini değiştirebilir
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(FORBIDDEN)
                .message("Bu işlem için yetkiniz bulunmamaktadır")
                .build()
                );
        }
        Sha256 sha256 = new Sha256();
        password = sha256.hash(password);
        us.sifreDegistir(u.getId(), password);
        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(e_mail+" e-postasına sahip kullanıcının şifresi değiştirildi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message(e_mail+" kullanıcısının şifresi güncellendi")
                .build()
                );
    }

    //E-posta adersi verilen kullancının puanını günceller
    @PostMapping("/update/point")
    public ResponseEntity<Response> updatePoint(HttpServletRequest request, @RequestParam String e_mail, @RequestParam int modifier){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();

        Users user = us.kullaniciyiBul(e_mail);
        user.setPoint(modifier+user.getPoint());
        us.puaniGuncelle(user.getPoint(), user);

        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(e_mail+" E-postasına sahip kullanıcının puanı güncellendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Puan güncellendi")
            .build()
            );
    }

    //Tüm puanları sıfırlar
    @PostMapping("/reset/points")
    public ResponseEntity<Response> updatePoint(HttpServletRequest request){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();

        us.puanlariResetle();

        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record("Puanlar resetlendi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Puanlar resetlendi.")
            .build()
            );
    }

    //E-posta adresi verilen kullanıcıyı aktiflik durumunu değiştirir
    @PostMapping("/active")
    public ResponseEntity<Response> updateActive(HttpServletRequest request, @RequestParam String e_mail){
        String jwt = request.getHeader(AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();

        Users user = us.kullaniciyiBul(e_mail);
        us.aktfilikDegistir(user.getId());

        Logs log = new Logs();
        log.setE_posta(username);
        log.setLog_record(e_mail+" kullancısı aktif statüsüne getirildi.");
        if(user.getId() > 0)
            log.setLog_record(e_mail+" kullancısı inaktif statüsüne getirildi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("Kullanıcının aktiflik durumu değiştirildi.")
            .build()
            );
    }
}
