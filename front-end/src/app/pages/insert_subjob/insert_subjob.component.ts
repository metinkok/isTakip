import { Component, OnInit } from '@angular/core';
import { JobService } from 'src/app/services/job.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-subjob-insert',
    templateUrl: './insert_subjob.component.html',
    styleUrls: ['./insert_subjob.component.css']
  })

  export class InsertSubjobComponent implements OnInit {
    data : Array<any> | undefined;
    constructor(private JobService: JobService, private Service : Service) { }

    ngOnInit(): void {

    }
    
    ngCreate(name: HTMLInputElement, status: HTMLSelectElement, exp: HTMLTextAreaElement, point: number, date: HTMLInputElement){
      var data ={
          id : -1,
          isim : name.value,
          status : status.value as unknown as number,
          aciklama : exp.value,
          point : point,
          parent : sessionStorage.getItem("parent_id") as unknown as number,
          deadline : date.value as unknown as Date
        };
      var stat = status.value as unknown as number;
      if(stat < 1)
          alert("Statü 1'den küçük olamaz")
      else if(stat > 3)
          alert("Statü 3'den büyük olamaz")
      else if(date.value == null || date.value == ""){
        alert("Deadline boş olamaz")
      }
      else{
        this.JobService.insertSubJob(data).subscribe((result) => {
          alert(result.message);
          location.assign("/job");
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