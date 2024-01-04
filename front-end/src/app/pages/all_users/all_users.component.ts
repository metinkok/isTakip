import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-all_users',
    templateUrl: './all_users.component.html',
    styleUrls: ['./all_users.component.css']
  })

  export class AllUsersComponent implements OnInit {
    data : Array<any> | undefined;
    constructor(private UserService: UserService, private Service : Service) { }

    ngOnInit(): void {
      if(localStorage.getItem("admin") == "admin"){
        location.replace("/users/admin")
      }
      else{
        this.UserService.list().subscribe((response) => {
            this.data = Array.from(Object.values(response.data));
            this.data.sort((a, b) => (a.isim > b.isim ? 1 : -1));
          },
          (error: HttpErrorResponse) => {
            var code = error.message
            location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
          });
        }
    }
    
    //Tüm kullanıcıları tekrar listelemek için boş arama yapılabilmesi için burada veritabanı isteği var tüm kullanıcıları tekrar listelemek için boş arama yapılabilmesi için 
    ngSearch(name: HTMLInputElement){
        this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
    }

    ngGive(id: string){
      sessionStorage.setItem("Atanan_id", id);
      location.assign("/jobs/give");
    }

    ngHome(){
      location.assign("/home");
    }
    ngJobs(){
      location.assign("/jobs");
    }
    ngNewJob(){
      location.assign("/jobs/add")
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