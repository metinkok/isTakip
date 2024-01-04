import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
@Injectable({
    providedIn: 'root'
  })
export class Service {
    private url = "http://localhost:8080/"//localhost urlsi
    private baseUrl = this.url+"api/"//istek için base url
    constructor(private http: HttpClient) { }

    //Spring sessionundan kullanıcıyı çeker
    getUser(): Observable<Response>{
        let information: Observable<Response>;
        information = this.http.get<Response>(this.baseUrl);
        return information;
      }

    //Apiye login isteği gönderir
    logoutRequest(): Observable<any>{
      let information: Observable<any>;
      information = this.http.get<any>(this.baseUrl+"logout");
      return information;
    }
    //default localhost:8080/api
    getUrl(): string{
        return this.baseUrl
    }
    //default localhost:8080
    getLocal(): string{
        return this.url
    }
}