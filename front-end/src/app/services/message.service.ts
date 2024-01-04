import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Jobs } from '../exports/Jobs';
import { Response } from '../exports/Response';
import { Service } from './service';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
    private baseUrl = this.Service.getUrl()+"message";
    constructor(private http: HttpClient, private Service : Service) { }

    //Idsi verilen işle aynı olan işin mesajlarını listeler
    listJobs(Jobs: Jobs): Observable<Response>{
        let information: Observable<Response>;
        information = this.http.get<Response>(this.baseUrl+"/"+Jobs.id);
        return information;
    }

    //Idsi verilen işle aynı olan işe yeni mesaj ekler
    insertMessage(Jobs: Jobs, message: string): Observable<Response>{
      let information: Observable<Response>;
      let body = new FormData();
      body.append("e_mail", ""+localStorage.getItem("User_mail"));
      body.append("message", message);
      information = this.http.post<Response>(this.baseUrl+"/"+Jobs.id+"/insert", body);
      return information;
    }

}