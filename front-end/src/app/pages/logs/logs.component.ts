import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Users } from 'src/app/exports/Users';
import { LogService } from 'src/app/services/log.service';
import { Service } from 'src/app/services/service';

let login:HTMLButtonElement=<HTMLButtonElement>document.getElementById("login");

@Component({
  selector: 'app-login',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})

export class LogsComponent implements OnInit {
    data : Array<any> | undefined;
    constructor(private LogService: LogService, private Service : Service) { }

  ngOnInit(): void {
    let today = new Date().toISOString().split('T')[0];
    this.LogService.listToday(today).subscribe((response) => {
      this.data = Array.from(Object.values(response.data));
    });
  } 
    
  ngSearch(name: HTMLInputElement){
    this.data = this.data?.filter(value => value.e_posta.lastIndexOf(name.value) >= 0);
  }

  ngYear(){
    this.LogService.listYear().subscribe((response) => {
      this.data = Array.from(Object.values(response.data));
    });
  }

  ngMonth(){
    this.LogService.listMonth().subscribe((response) => {
      this.data = Array.from(Object.values(response.data));
    });
  }

  ngToday(){
    let today = new Date().toISOString().split('T')[0];
    this.LogService.listToday(today).subscribe((response) => {
      this.data = Array.from(Object.values(response.data));
    });
  }
  
  ngBetween(day1: HTMLInputElement, day2: HTMLInputElement){
    this.LogService.listBetween(day1.value, day2.value).subscribe((response) => {
      this.data = Array.from(Object.values(response.data));
    });
  }

  ngDay(day: HTMLInputElement){
    let filterDay = day.value;
    this.LogService.listToday(filterDay).subscribe((response) => {
      this.data = Array.from(Object.values(response.data));
    });
  }

  ngNoFilter(){
    this.LogService.list().subscribe((response) => {
      this.data = Array.from(Object.values(response.data));
      });
  }

  ngPrint(){
    let aside = document.getElementById("side-bar");
    let search = document.getElementById("search");
    let row = document.getElementById("row");
    let day = document.getElementById("dayFilter");

    aside?.remove();
    search?.remove();
    row?.remove();
    day?.remove();
    window.print();

    this.LogService.insertLog("Log kayıtları yazdırıldı.").subscribe((response) => {
      location.assign("/logs");
    });
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