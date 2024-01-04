import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { LogService } from 'src/app/services/log.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.css']
  })

  export class UserComponent implements OnInit {
    data : Array<any> | undefined;
    e_mail : String | undefined;
    class: string |undefined;
    constructor(private UserService: UserService, private LogService: LogService, private Service : Service) { }

    ngOnInit(): void {
      this.class = "btn btn-block btn-secondary";
      var user ={
        id : -1,
        isim : "",
        e_posta : ""+sessionStorage.getItem("profile_user_mail"),
        sifre : "",
        point: -1,
        admn : -1

      };
      this.e_mail = ""+sessionStorage.getItem("profile_user_mail");
      let today = new Date().toISOString().split('T')[0];
      this.UserService.listThisMonth(user, today).subscribe((response) => {
        if(response.message == "8492"){
          location.assign("error/8492");
        }
        else{
          this.data = Array.from(Object.values(response.data));
          this.data.forEach(function (value){
            if(value.status == 3){
              value.status = "Bitmiş iş"
              value.class = "badge-danger"
            }
            else if(value.status == 2){
              value.status = "Üzerinde çalışılıyor"
              value.class = "badge-warning"
            }
            else if(value.status == 4){
              value.status = "İnaktif"
              value.class = "badge-secondary"
            }
            else{
              value.status = "Aktif iş"
              value.class = "badge-success"
            }
          });
          this.data.sort((a, b) => (a.isim > b.isim ? -1 : 1));
      }
      },
      (error: HttpErrorResponse) => {
        var code = error.message
        location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
      });
    }

    ngThisMonth(){
      let month = new Date().toISOString().split('T')[0];
      let year = new Date().toISOString().split('-')[0];
      month = month.slice(5)
      month = month.split('-')[0];
      month = year+'-'+month+'-'
      this.data = this.data?.filter(value => value.deadline.lastIndexOf(month) >= 0);
    }

    ngNoFilter(){
      var user ={
        id : -1,
        isim : "",
        e_posta : ""+sessionStorage.getItem("profile_user_mail"),
        sifre : "",
        point: -1,
        admn : -1

        };
        this.e_mail = user.e_posta;
        this.UserService.listOne(user).subscribe((response) => {
            this.data = Array.from(Object.values(response.data));
            this.data.forEach(function (value){
              if(value.status == 3){
                value.status = "Bitmiş iş"
                value.class = "badge-danger"
              }
              else if(value.status == 2){
                value.status = "Üzerinde çalışılıyor"
                value.class = "badge-warning"
              }
              else if(value.status == 4){
                value.status = "İnaktif"
                value.class = "badge-secondary"
              }
              else{
                value.status = "Aktif iş"
                value.class = "badge-success"
              }
            });
          },
          (error: HttpErrorResponse) => {
            var code = error.message
            location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
          });
    }

    delay(ms: number){
      return new Promise(resolve => setTimeout(resolve, ms));
    }

    beforePrint(){
      let after = document.getElementById("after");
      after?.inert;
      this.data?.forEach(function (value){
          value.class = ""
      });
    }

   async ngPrint(){
      this.class = "btn btn-block";
      let aside = document.getElementById("side-bar");
      let search = document.getElementById("search");
      let row1 = document.getElementById("row1");
      let row2 = document.getElementById("row2");

      aside?.remove();
      search?.remove();
      row1?.remove();
      row2?.remove();

      this.beforePrint();
      await this.delay(100);
      window.print();
      this.LogService.insertLog(sessionStorage.getItem("profile_user_mail")+" mailine sahip kullanıcının profili yazdırıldı.").subscribe((response) => {
        location.assign("/user");
      });
    }

    ngSearch(name: HTMLInputElement){
      this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
    }

    ngStatus(status: number){
      let stts = "Bitmiş iş";
      if(status == 1)
        stts = "Aktif iş";
      else if(status == 2)
        stts = "Üzerinde çalışılıyor"
      this.data = this.data?.filter(value => value.status == stts);
    }
    
    ngOpen(id: number): void{
      sessionStorage.setItem("job_id", ""+id);
      location.assign("/job/");
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