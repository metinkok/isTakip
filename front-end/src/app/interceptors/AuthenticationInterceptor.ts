import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';  
import { LoginService } from '../services/login.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
    providedIn: 'any'
  })
export class AuthenticationInterceptor implements HttpInterceptor {
      
    constructor(public auth: LoginService) {}  
    
    //Api isteklerini intercept edip isteğin headerına token ekler
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {  
      if(this.auth.getToken() != null){
        request = request.clone({  
            setHeaders: {  
            Authorization: `Bearer ${this.auth.getToken()}`  
            }  
        });
      }
    return next.handle(request);
    }
  }  