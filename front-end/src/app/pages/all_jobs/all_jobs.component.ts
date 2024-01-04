import { Component, OnInit, ViewChild } from '@angular/core';
import { JobService } from 'src/app/services/job.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';
import { getLocaleDateFormat, getLocaleDateTimeFormat } from '@angular/common';

@Component({
    selector: 'app-jobsAll',
    templateUrl: './all_jobs.component.html',
    styleUrls: ['./all_jobs.component.css']
  })

  export class AllJobsComponent implements OnInit {
    data : Array<any> | undefined;
    class: string | undefined;
    constructor(private JobService: JobService, private Service : Service) { }

    ngOnInit(): void {
        this.class = "btn btn-block btn-secondary";
        this.JobService.list().subscribe((response) => {
            this.data = Array.from(Object.values(response.data));
            this.data.forEach( (value) =>{
              this.JobService.usersInJob(value).subscribe((response) => {
                value.numberOfUsers = Array.from(Object.values(response.data)).length;
              })
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

    ngSearch(name: HTMLInputElement){
      this.data = this.data?.filter(value => value.isim.lastIndexOf(name.value) >= 0);
    }

    ngThisMonth(){
      let month = new Date().toISOString().split('T')[0];
      let year = new Date().toISOString().split('-')[0];
      month = month.slice(5)
      month = month.split('-')[0];
      month = year+'-'+month+'-'
      this.data = this.data?.filter(value => value.deadline.lastIndexOf(month) >= 0);
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
      let row = document.getElementById("row");

      aside?.remove();
      search?.remove();
      row?.remove();

      this.beforePrint();
      await this.delay(100);
      window.print();

      location.assign("/jobs");
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
    ngInactive(){
      location.assign("/jobs/inactive")
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