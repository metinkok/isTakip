import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Response } from '../exports/Response';
import { Service } from './service';

@Injectable({
  providedIn: 'root'
})
export class LogService {
    private baseUrl = this.Service.getUrl()+"logs";
    constructor(private http: HttpClient, private Service : Service) { }

    //Bütün logları listeler
    list(): Observable<Response>{
        let information: Observable<Response>;
        information = this.http.get<Response>(this.baseUrl+"/");
        return information;
    }

    //Bügünün loglarını listeler
    listToday(today: string): Observable<Response>{
      let information: Observable<Response>;
      let body = new FormData();
      body.append("today", today);
      information = this.http.post<Response>(this.baseUrl+"/today", body);
      return information;
  }
    //Bu ayın loglarını listeler
    listMonth(): Observable<Response>{
        let information: Observable<Response>;
        let body = new FormData();
        let month = new Date().toISOString().split('T')[0];
        let year = new Date().toISOString().split('-')[0];
        month = month.slice(5)
        month = month.split('-')[0];
        month = year+'-'+month+'-'
        body.append("month", month);
        information = this.http.post<Response>(this.baseUrl+"/month", body);
        return information;
    }

    //Verilen tarihler arasındaki logları listeler
    listBetween(day1: string, day2: string): Observable<Response>{
      let information: Observable<Response>;
      let body = new FormData();
      body.append("day1", day1);
      body.append("day2", day2);
      information = this.http.post<Response>(this.baseUrl+"/between", body);
      return information;
    }
    //Bu yılın loglarını listeler
    listYear(): Observable<Response>{
      let information: Observable<Response>;
      let body = new FormData();
      let year = new Date().toISOString().split('-')[0];
      body.append("year", year);
      information = this.http.post<Response>(this.baseUrl+"/year", body);
      return information;
    }

    //Yeni log ekler
    insertLog(message: string): Observable<Response>{
      let information: Observable<Response>;
      let body = new FormData();
      body.append("message", message);
      information = this.http.post<Response>(this.baseUrl+"/insert", body);
      return information;
  }
}