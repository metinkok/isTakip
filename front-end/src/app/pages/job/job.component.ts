import { Component, OnInit } from '@angular/core';
import { MessageService } from 'src/app/services/message.service';
import { Service } from 'src/app/services/service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from 'src/app/services/user.service';
import { Jobs } from 'src/app/exports/Jobs';
import { JobService } from 'src/app/services/job.service';
import { LogService } from 'src/app/services/log.service';

@Component({
    selector: 'app-job',
    templateUrl: './job.component.html',
    styleUrls: ['./job.component.css']
  })

  export class JobComponent implements OnInit {
    data : Array<any> | undefined;
    subJobs : Array<any> | undefined;
    users : Array<any> | undefined;
    job={
        id: -1,
        isim: "",
        aciklama: "",
        point: -1,
        status: -1,
        parent: -1,
        deadline: Date.now() as unknown as Date
    };
    status = "Aktif";
    constructor(private JobService: JobService,private MessageService: MessageService, private LogService: LogService, private UserService: UserService, private Service : Service) { };

    ngOnInit(): void {
      if(localStorage.getItem("admin") == "admin"){
        location.replace("/job/admin")
      }
      else{
        this.job.id = sessionStorage.getItem("job_id") as unknown as number;
        this.JobService.listOne(this.job).subscribe((response) => {
            if(response.message == "8492"){
              location.assign("error/8492");
            }
            else{
              let j = Array.from(Object.values(response.data));
              let job = j.at(0) as unknown as Jobs;
              this.job.isim = job.isim;
              this.job.aciklama = job.aciklama;
              this.job.status = job.status;
              this.job.point = job.point;
              if(job.parent != null){
                this.job.parent = job.parent;
              }
              else{
                let button = document.getElementById("parentButton");
                button?.remove();
              }
              if(job.status == 3){
                  this.status = "Bitmiş iş"
                  let button2 = document.getElementById("button2");
                  let button4 = document.getElementById("button4");
                  let button5 = document.getElementById("button5");
                  button2?.remove();
                  button4?.remove();
                  button5?.remove();
              }
              else if(job.status == 4){
                this.status = "İnaktif"
                let button2 = document.getElementById("button2");
                let button3 = document.getElementById("button3");
                let button4 = document.getElementById("button4");
                let button5 = document.getElementById("button5");
                button2?.remove();
                button3?.remove();
                button4?.remove();
                button5?.remove();
              }
              else if(job.status == 2)
                  this.status = "Üzerinde çalışılıyor"
              this.job.deadline = job.deadline;
            }
         });
        this.JobService.listSubJobs(this.job.id).subscribe((response) => {
          this.subJobs = Array.from(Object.values(response.data));
          if(this.subJobs.length == 0){
            let table = document.getElementById("sj");
            table?.remove();
          }
          else{
            this.subJobs.forEach( (value) =>{
              if(value.status == 1)
                value.class = "btn btn-block btn-success"
              else if(value.status == 2)
                value.class = "btn btn-block btn-warning"
              else if(value.status == 3)
                value.class = "btn btn-block btn-danger"
              else
                value.class = "btn btn-block btn-secondary"
            });
          }
        });
        this.MessageService.listJobs(this.job).subscribe((response) => {
            this.data = Array.from(Object.values(response.data));
            this.data.forEach( (value) =>{
                this.UserService.find(value.user_id).subscribe((usr) => {
                    let user = Array.from(Object.values(usr.data))
                    value.user = user.at(0);
                    value.isim = value.user.isim;
                    value.e_posta = value.user.e_posta;
                    value.class = "card card-secondary";

                    if(value.mesaj.lastIndexOf("::BLUE::") >= 0){
                      value.class = "card card-primary";
                      value.mesaj = value.mesaj.substring("::BLUE::".length);
                    }
                    else if(value.mesaj.lastIndexOf("::YELLOW::") >= 0){
                      value.class = "card card-warning";
                      value.mesaj = value.mesaj.substring("::YELLOW::".length);
                    }
                    else if(value.mesaj.lastIndexOf("::RED::") >= 0){
                      value.class = "card card-danger";
                      value.mesaj = value.mesaj.substring("::RED::".length);
                    }
                    else if(value.mesaj.lastIndexOf("::GREEN::") >= 0){
                      value.class = "card card-success";
                      value.mesaj = value.mesaj.substring("::GREEN::".length);
                    }
                });
            });
            this.data.sort((a, b) => (a.id > b.id ? -1 : 1));
          },
          (error: HttpErrorResponse) => {
            var code = error.message
            location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
          });
          
          this.JobService.usersInJob(this.job).subscribe((response) => {
            this.users = Array.from(Object.values(response.data));
          },
          (error: HttpErrorResponse) => {
            var code = error.message
            location.assign("error/"+code.substring(code.lastIndexOf(':')+2,code.lastIndexOf(':')+5))
          });
        }
    }

    ngMessage(message: HTMLTextAreaElement){
      if(message.value == "" || message.value == null)
        alert("Boş mesaj bırakılamaz");
      else{
        sessionStorage.setItem("job_id", ""+this.job.id);
        let job = {
          id : this.job.id,
          isim : "",
          status : -1,
          aciklama : "",
          point: -1,
          parent: -1,
          deadline: Date.now() as unknown as Date
      }
        this.MessageService.insertMessage(job, message.value).subscribe((result) => {
            if(result.message == "Mesaj eklendi")
              location.assign("/job");
            else
              alert(result.message);
        });
      }
    }

    ngTake(){
      if(this.job.status == 3)
        alert("Bitmiş iş alınamaz.")
      else{
        this.JobService.take(this.job).subscribe((response) => { 
          if(response.message == "İş alındı."){
            location.assign("/job");
          }
          else
            alert(response.message)
        });
      }
    }

    ngGive(){
      if(this.job.status == 3)
        alert("Bitmiş iş atanamaz.")
      else
        location.replace("/assign");
    }
    
    ngDrop(){
      if(this.job.status == 3)
        alert("Bitmiş iş bırakılamaz.")
      else{
        this.JobService.drop(this.job).subscribe((response) => { 
          if(response.message == "Görev bırakıldı"){
            location.assign("/job");
          }
          else
            alert(response.message)
        });
      }
    }
    
    ngFinish(){
      if(this.job.status == 3)
        alert("İşin statüsü zaten tamamlanmış.")
      else{
        this.job.status = 3;
        this.JobService.statusChange(this.job).subscribe((response) => { 
          if(response.message == this.job.isim+" isimli işin statüsü Tamamlandı olarak güncellendi.")
            location.assign("/job");
        });
        this.users?.forEach(user => {
          this.UserService.updatePoint(user, this.job.point).subscribe((response) =>{

          });
        })
      }
    }
    
    ngWorking(){
      if(this.job.status == 2)
        alert("İşin statüsü zaten üzerinde çalışılıyor.")
      else if(this.job.status == 3){
        this.users?.forEach(user => {
          let modifier = -1;
          modifier = modifier * this.job.point;
          this.UserService.updatePoint(user, modifier).subscribe((response) =>{
          });
        })
        this.job.status = 2;
        this.JobService.statusChange(this.job).subscribe((response) => { 
          if(response.message == this.job.isim+" isimli işin statüsü Üzerinde çalışılıyor olarak güncellendi.")
            location.assign("/job");
        });
      }
      else{
        this.job.status = 2;
        this.JobService.statusChange(this.job).subscribe((response) => { 
          if(response.message == this.job.isim+" isimli işin statüsü Üzerinde çalışılıyor olarak güncellendi.")
            location.assign("/job");
        });
      }
    }
    
    ngActive(){
      if(this.job.status == 1)
        alert("İşin statüsü zaten aktif.")
      else if(this.job.status == 3){
        this.users?.forEach(user => {
          let modifier = -1;
          modifier = modifier * this.job.point;
          this.UserService.updatePoint(user, modifier).subscribe((response) =>{
          });
        })
        this.job.status = 1;
        this.JobService.statusChange(this.job).subscribe((response) => { 
          if(response.message == this.job.isim+" isimli işin statüsü Aktif olarak güncellendi.")
            location.assign("/job");
        });
      }
      else{
        this.job.status = 1;
        this.JobService.statusChange(this.job).subscribe((response) => { 
          if(response.message == this.job.isim+" isimli işin statüsü Aktif olarak güncellendi.")
            location.assign("/job");
        });
      }
    }

    ngSubjob(id:number){
      sessionStorage.setItem("job_id", ""+id);
      location.assign("/job/");
    }
    ngParent(){
      sessionStorage.setItem("job_id", ""+this.job.parent);
      location.assign("/job/");
    }
    ngSubJob(){
      sessionStorage.setItem("parent_id", ""+this.job.id);
      location.assign("/job/insert/subjob");
    }

    ngFilterAutomated(){
      this.data = this.data?.filter(value => value.mesaj.lastIndexOf("kullanıcısı almıştır.") < 0);
      this.data = this.data?.filter(value => value.mesaj.lastIndexOf("kullanıcısına atanmıştır.") < 0);
      this.data = this.data?.filter(value => value.mesaj.lastIndexOf("Görev bırakıldı.") < 0);
      this.data = this.data?.filter(value => value.mesaj.lastIndexOf(" olarak güncellendi.") < 0);
    }

    ngFilter(id: number){
      this.data = this.data?.filter(value => value.user_id == id);
    }

    delay(ms: number){
      return new Promise(resolve => setTimeout(resolve, ms));
    }

    beforePrint(){
      let after = document.getElementById("after");
      after?.inert;
      this.data?.forEach(function (value){
          value.class = "card card-secondary"
      });

    }

   async ngPrint(){
      let aside = document.getElementById("side-bar");
      let row1 = document.getElementById("row1");
      let row2 = document.getElementById("row2");
      let button = document.getElementById("parentButton");
      let button1 = document.getElementById("button1");
      let button2 = document.getElementById("button2");
      let button3 = document.getElementById("button3");
      let button4 = document.getElementById("button4");
      let button5 = document.getElementById("button5");
      let button6 = document.getElementById("addSubJob");

      aside?.remove();
      row1?.remove();
      row2?.remove();
      button?.remove();
      button1?.remove();
      button2?.remove();
      button3?.remove();
      button4?.remove();
      button5?.remove();
      button6?.remove();

      this.beforePrint();
      await this.delay(100);
      window.print();

      this.LogService.insertLog(sessionStorage.getItem("job_id")+" Idli iş yazdırıldı.").subscribe((response) => {
        location.assign("/job");
      });
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