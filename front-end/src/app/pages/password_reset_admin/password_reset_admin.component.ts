import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-password-reset-admin',
    templateUrl: './password_reset_admin.component.html',
    styleUrls: ['./password_reset_admin.component.css']
  })

  export class PasswordResetAdmin implements OnInit {
    data : Array<any> | undefined;
    constructor(private UserService: UserService, private Service : Service) { }

    ngOnInit(): void {
      if(localStorage.getItem("admin") != "admin"){
          location.replace("/error/403")
      }
    }
    
    ngPasswordReset(password: HTMLInputElement, password2: HTMLInputElement){
        if(password.value != password2.value)
            alert("Şifreler aynı değil");
        else{
            var user ={
                id: -1,
                isim: "",
                e_posta: ""+sessionStorage.getItem("password_user_mail"),
                sifre: "",
                point: -1,
                admn: -1
            }
            this.UserService.resetPasswordAdmin(user, password.value).subscribe((result) => {
              alert(result.message)
              location.assign("/users/admin");
            },
            (error: HttpErrorResponse) => {
              var code = error.message
              localStorage.removeItem("admin");
              location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
            });
            
        }
    }

    ngHome(){
      location.assign("/home/admin");
    }
    ngJobs(){
      location.assign("/jobs");
    }
    ngUsers(){
      location.assign("/users/admin");
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