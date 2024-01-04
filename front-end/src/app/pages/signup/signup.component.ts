import { Component, OnInit } from '@angular/core';
import { SignupService } from 'src/app/services/signup.service';
import { Service } from 'src/app/services/service';

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
  })

  export class SignUpComponent implements OnInit {
    constructor(private SignupService: SignupService, private Service : Service) { }

    ngOnInit(): void {}
    
    ngSendData(username: HTMLInputElement, password: HTMLInputElement, name: HTMLInputElement): void {
        var data ={
          userName : username.value,
          password : password.value,
          name : name.value
        };
        if(data.userName == "")
            alert("Kullanıcı adı boş bırakılamaz")
        else if(data.password == "")
            alert("Şifre alanı boş bırakılamaz")
        else if(data.name == "")
            alert("İsim alanı boş bırakılamaz")
        else{
            this.SignupService.signupRequest(data).subscribe((result) => {
                if(result == null)
                  alert("Girmiş olduğunuz e-posta adresi kullanılıyor")  
                else if(result.message == "Yeni kullanıcı eklendi"){
                    location.assign("/");
                  }
                else{
                  alert(result.message);
                }
              });
            location.assign("/");
        }
    }

  }