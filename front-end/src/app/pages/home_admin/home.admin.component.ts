import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { JobService } from 'src/app/services/job.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-home-admin',
    templateUrl: './home.admin.component.html',
    styleUrls: ['./home.admin.component.css']
  })

  export class HomeAdminComponent implements OnInit {
    data : Array<any> | undefined;
    user : string | undefined;
    class: string | undefined;
    constructor(private UserService: UserService, private JobService: JobService, private Service : Service) { }

    ngOnInit(): void {
      this.class = "btn btn-block btn-secondary";
      if(localStorage.getItem("admin") == "admin"){
          var user ={
              id : -1,
              isim : "",
              e_posta : ""+localStorage.getItem("User_mail"),
              sifre : "",
              point: -1,
              admn : -1

          };
          this.user = user.e_posta;
          let today = new Date().toISOString().split('T')[0];
          this.UserService.listThisMonth(user, today).subscribe((response) => {
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
            this.data.sort((a, b) => (a.deadline > b.deadline ? -1 : 1));
            },
            (error: HttpErrorResponse) => {
              var code = error.message
              location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
            });
          }
          else
            location.assign("/home");
    }

    ngThisMonth(){
      let month = new Date().toISOString().split('T')[0];
      let year = new Date().toISOString().split('-')[0];
      month = month.slice(5)
      month = month.split('-')[0];
      month = year+'-'+month+'-'
      this.data = this.data?.filter(value => value.deadline.lastIndexOf(month) >= 0);
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

      location.assign("/home");
    }

    ngNoFilter(){
      if(localStorage.getItem("admin") == "admin"){
        var user ={
            id : -1,
            isim : "",
            e_posta : ""+localStorage.getItem("User_mail"),
            sifre : "",
            point: -1,
            admn : -1

        };
        this.user = user.e_posta;
        this.UserService.listAdmin(user).subscribe((response) => {
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
              else if(value.status == 4){
                value.status = "İnaktif"
                value.class = "badge-secondary"
              }
              else{
                value.status = "Aktif iş"
                value.class = "badge-success"
              }
          });
          this.data.sort((a, b) => (a.deadline > b.deadline ? -1 : 1));
          },
          (error: HttpErrorResponse) => {
            var code = error.message
            location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
          });
        }
        else
          location.assign("/home");
    }

    ngStatus(status: number){
      let stts = "Bitmiş iş";
      if(status == 1)
        stts = "Aktif iş";
      else if(status == 2)
        stts = "Üzerinde çalışılıyor"
      this.data = this.data?.filter(value => value.status == stts);
    }

    ngSearch(name: HTMLInputElement){
      this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
    }
    
    ngOpen(id: number): void{
      sessionStorage.setItem("job_id", ""+id);
      location.assign("/job");
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