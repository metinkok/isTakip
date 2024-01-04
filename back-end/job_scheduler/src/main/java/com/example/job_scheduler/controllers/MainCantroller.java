package com.example.job_scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.job_scheduler.Sha256;
import com.example.job_scheduler.entities.Logs;
import com.example.job_scheduler.entities.Users;
import com.example.job_scheduler.entities.Users_Jobs;
import com.example.job_scheduler.services.Logs_Service;
import com.example.job_scheduler.services.Users_Jobs_Service;
import com.example.job_scheduler.services.Users_Service;
import com.example.job_scheduler.model.Response;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.job_scheduler.Config;
import com.example.job_scheduler.Login;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class MainCantroller {
    @Autowired
    private Users_Service us;
    @Autowired
    private Users_Jobs_Service ujs;
    @Autowired
    private Logs_Service ls;
    private Sha256 sha256 = new Sha256();

    //Login için metod
    public ResponseEntity<Response> loginPage(Login login){
        if(login != null){
            return attemptLogin(login);
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(BAD_REQUEST)
            .message("Kullanıcı adı ve/veya şifre alanı boş")
            .build()
            );
    }

    //Kullanıcının giriş yapmasını sağlar
    @PostMapping("/login")
    public ResponseEntity<Response> attemptLogin(Login login){
        boolean admLogin = false;
        login.setPassword(sha256.hash(login.getPassword()));
        if(login.getUserName().indexOf(".admin") == login.getUserName().length()-6){
            admLogin = true;
            login.setUserName(login.getUserName().substring(0, login.getUserName().length()-6));
        }
        Users user = us.kullaniciyiBul(login.getUserName());
        if(user == null){
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(BAD_REQUEST)
                .message("Bu e-posta adresinde kullanıcı bulumamaktadır")
                .build()
                );
        }
        if(user.getActive() == -1){
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("Hesabınız inaktif durumdadır, aktive etmek için sistem sorumlunuzla görüşmelisiniz.")
                .id(user.getId())
                .build()
                );
        }
        if(user.getSifre().equals(login.getPassword())){
            user.setSifre("");
            if(admLogin){
                if(user.getAdmn() != 1){
                    Logs log = new Logs();
                    log.setE_posta(login.getUserName());
                    log.setLog_record("Hatalı işlem: Hatalı giriş denemesi.");
                    log.setDate_time(""+LocalDateTime.now());
                    ls.logEkle(log);
                    return ResponseEntity.ok(
                        Response.builder()
                        .time(now())
                        .httpStatus(OK)
                        .message("Kullanıcının admin yetkisi bulunmamaktadır")
                        .id(user.getId())
                        .build()
                        );
                }
            }
            login.setAdmin(user.getAdmn());
            Logs log = new Logs();
            log.setE_posta(login.getUserName());
            log.setLog_record("Giriş yapıldı.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                    Response.builder()
                    .time(now())
                    .httpStatus(OK)
                    .message("Oturum açma işlemi başarılı")
                    .id(user.getId())
                    .build()
                    );
        }
        Logs log = new Logs();
        log.setE_posta(login.getUserName());
        log.setLog_record("Hatalı işlem: Hatalı giriş denemesi.");
        log.setDate_time(""+LocalDateTime.now());
        ls.logEkle(log);
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(BAD_REQUEST)
            .message("E-posta veya parola hatalı")
            .build()
            );
    }

    //Sessiondaki kullanıcıyi döner(Stateless olduğu için null döner)
    @GetMapping("")
    public ResponseEntity<Response> getSessionUser(HttpSession session){
        Users user = (Users) session.getAttribute("User");
        if(user == null)
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(BAD_REQUEST)
            .message("Aktif kullanıcı bulunmamaktadır")
            .build()
            );
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

    //Aktif oturumu kapatır
    @GetMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request, HttpSession session){
        String jwt = request.getHeader(AUTHORIZATION);
        if((jwt == null) || (jwt == ""))
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(BAD_REQUEST)
            .message("Açık oturum bulunmamaktadır")
            .build()
            );
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Config.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_token = verifier.verify(token);
        String username = decoded_token.getSubject();
            session.setAttribute("User", null);
            Logs log = new Logs();
            log.setE_posta(username);
            log.setLog_record("Çıkış yapıldı.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("Oturum sonlandırılmıştır")
                .build()
                );
    }

    //Yeni kullanıcı ekler
    @PostMapping("/signup")
    public ResponseEntity<Response> attemptSignup(@RequestParam String e_mail, @RequestParam String password, @RequestParam String name){
        if(us.kullaniciyiBul(e_mail) != null){
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(BAD_REQUEST)
                .message("E-posta adresi zaten kayıtlı")
                .build()
                );
        }
        else{
            Users user = new Users();
            password = sha256.hash(password);
            user.setSifre(password);
            user.setE_posta(e_mail);
            user.setIsim(name);
            us.kullaniciEkle(user);
            Logs log = new Logs();
            log.setE_posta(e_mail);
            log.setLog_record("Yeni kullanıcı kaydı oluşturuldu.");
            log.setDate_time(""+LocalDateTime.now());
            ls.logEkle(log);
            return ResponseEntity.ok(
                Response.builder()
                .time(now())
                .httpStatus(OK)
                .message("Yeni kullanıcı eklendi")
                .build()
                );
        }
    }

    //E_mail adresi verilen kullanıcnın admin olup olmadığı bilgisini döner
    public boolean isAdmin(String e_mail) {
        Users user = us.kullaniciyiBul(e_mail);
        if(user != null)
            if(user.getAdmn() == 1)
                return true;
        return false;
    }

    //Tüm adminlere idsi verilen işi atar
    @PostMapping("/admins")
    public ResponseEntity<Response> adminlereAta(@RequestParam int job_id){
        List<Users> admins = us.adminleriBul();
        for(int i = 0; i<admins.size(); i++){
            Users_Jobs uj = new Users_Jobs(admins.get(i).getId(), job_id);
            ujs.kullaniciIsEkle(uj);
        }
        return ResponseEntity.ok(
            Response.builder()
            .time(now())
            .httpStatus(OK)
            .message("İş atandı")
            .build()
            );
    }
}
