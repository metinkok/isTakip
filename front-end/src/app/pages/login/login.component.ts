import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Users } from 'src/app/exports/Users';
import { LoginService } from 'src/app/services/login.service';
import { Service } from 'src/app/services/service';

let login:HTMLButtonElement=<HTMLButtonElement>document.getElementById("login");

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  constructor(private LoginService: LoginService, private Service : Service) { }

  ngOnInit(): void {
  } 

  ngSendData(username: HTMLInputElement, password: HTMLInputElement): void {
    var data ={
      userName : username.value,
      password : password.value
    };
    if(data.userName == "")
      alert("Kullanıcı adı boş bırakılamaz")
    else if(data.password == "")
      alert("Şifre alanı boş bırakılamaz")
    else{
      this.LoginService.loginRequest(data).subscribe((result) => {
        if(result == null)
          alert("Kullanıcı adı veya şifre hatalı");
        else{
            localStorage.clear();
            sessionStorage.clear();
            alert("Oturum açıldı")
              if(username.value.indexOf(".admin") == username.value.length-6){
                username.value = username.value.substring(0, username.value.length-6);
                sessionStorage.setItem("User_mail", username.value);
                sessionStorage.setItem("token", result.access_token);
                sessionStorage.setItem("admin", "admin");
                localStorage.setItem("User_mail", username.value);
                localStorage.setItem("token", result.access_token);
                localStorage.setItem("admin", "admin");
                console.log("Logged in user with e_mail: "+sessionStorage.getItem("User_mail")+".admin");
                location.assign("/home/admin");
              }
              else{
                sessionStorage.setItem("User_mail", username.value);
                sessionStorage.setItem("token", result.access_token);
                localStorage.setItem("User_mail", username.value);
                localStorage.setItem("token", result.access_token);
                console.log("Logged in user with e_mail: "+sessionStorage.getItem("User_mail"));
                location.assign("/home");
            }
        }
      });
    }
  }

  ngSignUp(){
    location.assign("/signup");
  }
}

