import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { LogService } from 'src/app/services/log.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-all_users-admin',
    templateUrl: './all_users_admin.component.html',
    styleUrls: ['./all_users_admin.component.css']
  })

  export class AllUsersAdminComponent implements OnInit {
    data : Array<any> | undefined;
    constructor(private UserService: UserService, private LogService: LogService, private Service : Service) { }

    ngOnInit(): void {
      if(localStorage.getItem("admin") == "admin"){
        this.UserService.listWithAdmin().subscribe((response) => {
            this.data = Array.from(Object.values(response.data));
            this.data.sort((a, b) => (a.isim > b.isim ? 1 : -1));
            this.data.forEach( (value) =>{
              if(value.admn == 1){
                value.button = "Adminlik yetkisini al"
                value.class = "table-danger"
              }
              else{
                value.button = "Adminlik yetkisi ver"
                value.class = ""
              }
              if(value.active < 0)
                value.class = "table-secondary"
            });
          },
          (error: HttpErrorResponse) => {
            var code = error.message
            location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
          });
        }
        else{
          location.replace("error/403");
        }
    }

    ngShow(e_posta: string){
      sessionStorage.setItem("profile_user_mail", e_posta);
      location.assign("/user");
    }
    
    ngPasswordReset(e_posta: string){
        sessionStorage.setItem("password_user_mail", e_posta);
        location.assign("/user/admin/password_reset");
    }
    
    ngGive(id: string){
      sessionStorage.setItem("Atanan_id", id);
      location.assign("/jobs/give");
    }
    
    //Tüm kullanıcıları tekrar listelemek için boş arama yapılabilmesi için burada veritabanı isteği var tüm kullanıcıları tekrar listelemek için boş arama yapılabilmesi için 
    ngSearch(name: HTMLInputElement){
      this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
    }
    
    ngAdmin(e_posta: string, admin: number){
      if(e_posta == localStorage.getItem("User_mail"))
        alert("Kendi yetkinizi alamazsınız");
      else{
        let user ={
          id: -1,
          isim : "",
          e_posta : e_posta,
          sifre : "",
          point: -1,
          admn : -1
        }
        if(admin == -1){
            this.UserService.giveAdmin(user).subscribe((response) => {
                alert(response.message)
                location.assign("/users/admin");
            },
            (error: HttpErrorResponse) => {
              var code = error.message
              location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
            });
        }
        else{
          this.UserService.revokeAdmin(user).subscribe((response) => {
            alert(response.message)
            location.assign("/users/admin");
        },
        (error: HttpErrorResponse) => {
          var code = error.message
          location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
        });
        }
      }
    }

    ngFilterInactive(){
      this.data = this.data?.filter(value => value.active > 0);
    }

    ngChangeActive(e_posta: string){
      this.UserService.changeActive(e_posta).subscribe((response) => {
        alert(response.message);
        location.assign("users/admin");
      })
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
      let aside = document.getElementById("side-bar");
      let search = document.getElementById("search");
      let button1 = document.getElementById("button1");
      let button2 = document.getElementById("button2");
      let button3 = document.getElementById("button3");

      aside?.remove();
      search?.remove();
      button1?.remove();
      button2?.remove();
      button3?.remove();

      this.beforePrint();
      await this.delay(100);
      window.print();
      this.LogService.insertLog("Kullanıcı listesi yazdırıldı").subscribe((response) => {
        location.assign("/users/admin");
      });
    }


    ngReset(){
      this.UserService.resetPoints().subscribe((response) => {
        alert(response.message);
        location.assign("/users/admin");
      });
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