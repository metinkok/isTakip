import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-password-reset',
    templateUrl: './password_reset.component.html',
    styleUrls: ['./password_reset.component.css']
  })

  export class PasswordReset implements OnInit {
    data : Array<any> | undefined;
    constructor(private UserService: UserService, private Service : Service) { }

    ngOnInit(): void {}
    
    ngPasswordReset(password: HTMLInputElement, password2: HTMLInputElement){
        if(password.value != password2.value)
            alert("Şifreler aynı değil");
        else{
            this.UserService.resetPassword(password.value).subscribe((result) => {
              alert(result.message)
              location.assign("/home");
            });
        }
    }

    ngHome(){
      location.assign("/home");
    }
    ngJobs(){
      location.assign("/jobs");
    }
    ngUsers(){
      location.assign("/users");
    }
    ngPassword(){
      location.assign("users/password/reset")
    }
    ngLogs(){
      location.assign("/logs")
    }   
    
    ngExit(){
      this.Service.logoutRequest().subscribe((response) => {
        if(response.message == "Oturum sonlandırılmıştır"){
          localStorage.clear();
          sessionStorage.clear();
          location.assign("/");
        }
        else
          alert(response.message)
      });
    }
}