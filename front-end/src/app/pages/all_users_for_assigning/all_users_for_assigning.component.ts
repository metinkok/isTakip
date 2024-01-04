import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { JobService } from 'src/app/services/job.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-all_users_for_assigning',
    templateUrl: './all_users_for_assigning.component.html',
    styleUrls: ['./all_users_for_assigning.component.css']
  })
  export class AllUsersForAssigning implements OnInit {
    data : Array<any> | undefined;
    constructor(private UserService: UserService, private JobService:JobService, private Service : Service) { }

    ngOnInit(): void {
        this.UserService.list().subscribe((response) => {
            this.data = Array.from(Object.values(response.data));
            this.data.sort((a, b) => (a.isim > b.isim ? 1 : -1));
          },
          (error: HttpErrorResponse) => {
            var code = error.message
            location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
          });
    }
    ngSearch(name: HTMLInputElement){
        this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
    }
    
    ngAssign(){
        let job = {
            id : sessionStorage.getItem("job_id") as unknown as number,
            isim : "",
            status : -1,
            aciklama : "",
            point: -1,
            parent: -1,
            deadline: Date.now() as unknown as Date
        }
        var markedBoxes = document.querySelectorAll('input[type = "checkbox"]:checked')
        markedBoxes.forEach(box => {
            let user ={
                id: box.id as unknown as number,
                isim : "",
                e_posta : ""+box.id,
                sifre : "",
                point: -1,
                admn : -1
            }
            this.JobService.give(job, user).subscribe((result) => {
                if(result.message != "İş ataması başarılı."){
                    alert(result.message);
                }
            },
            (error: HttpErrorResponse) => {
              var code = error.message
              localStorage.removeItem("admin");
              location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
            });
        });
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