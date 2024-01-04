import { Component, OnInit, ViewChild } from '@angular/core';
import { JobService } from 'src/app/services/job.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-jobsAllAdmin',
    templateUrl: './all_jobs_admin.component.html',
    styleUrls: ['./all_jobs_admin.component.css']
  })

  export class AllJobsAdminComponent implements OnInit {
    data : Array<any> | undefined;
    constructor(private JobService: JobService, private Service : Service) { }

    ngOnInit(): void {
        this.JobService.list().subscribe((response) => {
            this.data = Array.from(Object.values(response.data));
            this.data = this.data.filter(value => value.status != 3)
            this.data.forEach( (value) =>{
              this.JobService.usersInJob(value).subscribe((response) => {
                value.numberOfUsers = Array.from(Object.values(response.data)).length;
              });
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

    ngThisMonth(){
      let month = new Date().toISOString().split('T')[0];
      let year = new Date().toISOString().split('-')[0];
      month = month.slice(5)
      month = month.split('-')[0];
      month = year+'-'+month+'-'
      this.data = this.data?.filter(value => value.deadline.lastIndexOf(month) >= 0);
    }
    
    ngNoFilter(){
      this.JobService.list().subscribe((response) => {
        this.data = Array.from(Object.values(response.data));
        this.data.forEach( (value) =>{
          if(value.status == 3){
            value.status = "Bitmiş iş"
            value.class = "badge-danger"
          }
          else if(value.status == 2){
            value.status = "Üzerinde çalışılıyor"
            value.class = "badge-warning"
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

    ngStatus(status: number){
      let stts = "Bitmiş iş";
      if(status == 1)
        stts = "Aktif iş";
      else if(status == 2)
        stts = "Üzerinde çalışılıyor"
      this.data = this.data?.filter(value => value.status == stts);
    }

    ngGive(id: number): void{
        let user={
            id: sessionStorage.getItem("Atanan_id") as unknown as number,
            isim : "",
            e_posta : ""+localStorage.getItem("User_mail"),
            sifre : "",
            point: -1,
            admn : -1
        }
        let job = {
            id : id,
            isim : "",
            status : -1,
            aciklama : "",
            point: -1,
            parent: -1,
            deadline: Date.now() as unknown as Date
        }
        this.JobService.give(job, user).subscribe((result) => {
            if(result.message != "İş ataması başarılı."){
                alert(result.message);
                location.replace("/users")
            }
            else{
                sessionStorage.setItem("job_id", ""+id);
                location.replace("/job")
            }
        },
        (error: HttpErrorResponse) => {
          var code = error.message
          localStorage.removeItem("admin");
          location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
        });
    }

    ngSearch(name: HTMLInputElement){
      this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
    }

    ngHide(){
      let today = new Date().toISOString().split('T')[0];
      this.JobService.listNotEnded(today).subscribe((response) => {
          this.data = Array.from(Object.values(response.data));
          this.data.sort((a, b) => (a.deadline > b.deadline ? -1 : 1));
          this.data.forEach( (value) =>{
            if(value.status == 3){
              value.status = "Bitmiş iş"
              value.class = "badge-danger"
            }
            else if(value.status == 2){
              value.status = "Üzerinde çalışılıyor"
              value.class = "badge-warning"
            }
            else{
              value.status = "Aktif iş"
              value.class = "badge-success"
            }
          });
          this.data.sort((a, b) => (a.deadline > b.deadline ? 1 : -1));
        },
        (error: HttpErrorResponse) => {
          var code = error.message
          location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
        });
    }
    
    ngToday(){
      let today = new Date().toISOString().split('T')[0];
      this.data = this.data?.filter(value => value.deadline.lastIndexOf(today) >= 0);
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