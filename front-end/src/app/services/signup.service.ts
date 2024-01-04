import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Signup } from '../exports/Signup';
import { Response } from '../exports/Response';
import { Service } from './service';

@Injectable({
    providedIn: 'root'
  })
export class SignupService {
  private baseUrl = this.Service.getUrl()+"signup";
  constructor(private http: HttpClient, private Service : Service) { }

  //Apiye yeni kullanıcı kaydı isteği gönderir
  signupRequest(signup: Signup): Observable<any>{
    let information: Observable<any>;
    let body = new FormData();
    body.append("e_mail", signup.userName);
    body.append("name", signup.name);
    body.append("password", signup.password);
    information = this.http.post<any>(this.baseUrl, body);
    return information;
  }
}