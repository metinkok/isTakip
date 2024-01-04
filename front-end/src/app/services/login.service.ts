import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Login } from '../exports/Login';
import { Service } from './service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private baseUrl = this.Service.getUrl()+"login";
  constructor(private http: HttpClient, private Service : Service) { }

  //Apiye login isteği gönderir
  loginRequest(login: Login): Observable<any>{
    let information: Observable<any>;
    let body = new FormData();
    body.append("e_mail", login.userName);
    body.append("password", login.password);
    information = this.http.post<any>(this.baseUrl, body);
    return information;
  }

  //Giriş yapıldıktan sonra local storage'a atanan tokenı döner
  getToken():String | any{
    return localStorage.getItem("token");
  }
}

