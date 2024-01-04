import { Component, OnInit, ViewChild } from '@angular/core';
import { JobService } from 'src/app/services/job.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-jobsAllInactive',
    templateUrl: './all_jobs_inactive.component.html',
    styleUrls: ['./all_jobs_inactive.component.css']
  })

  export class AllJobsInactiveComponent implements OnInit {
    data : Array<any> | undefined;
    constructor(private JobService: JobService, private Service : Service) { }

    ngOnInit(): void {
        this.JobService.listInactive().subscribe((response) => {
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

    ngSearch(name: HTMLInputElement){
      this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
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