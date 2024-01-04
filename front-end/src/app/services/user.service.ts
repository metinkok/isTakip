import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Users } from '../exports/Users';
import { Response } from '../exports/Response';
import { Service } from './service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
    private baseUrl = this.Service.getUrl()+"users";
    constructor(private http: HttpClient, private Service : Service) { }

    //Bütün kullanıcıları listeler
    list(): Observable<Response>{
        let information: Observable<Response>;
        information = this.http.get<Response>(this.baseUrl+"/");
        return information;
      }

    //Bütün kullanıcıları listeler Admin bilgisini tutar
    listWithAdmin(): Observable<Response>{
      let information: Observable<Response>;
      information = this.http.get<Response>(this.baseUrl+"/admin");
     return information;
    }

    //E-posta adresi verilen kullanıcıyla aynı olan kullanıcının işlerini listeler
    listOne(Users: Users): Observable<Response>{
        let information: Observable<Response>;
        information = this.http.get<Response>(this.baseUrl+"/"+Users.e_posta);
        return information;
      }

    //E-posta adresi verilen kullanıcıyla aynı olan kullanıcının işlerinden statüsü verilen statüde olan işleri listeler
    listWithStatus(Users: Users, status: number): Observable<Response>{
      let information: Observable<Response>;
      information = this.http.get<Response>(this.baseUrl+"/"+Users.e_posta+"/"+status);
      return information;
    }

    //E-posta adresi verilen kullanıcıyla aynı olan kullanıcının işlerinden deadline'ı verilen deadline ile aynı ayda olan işleri listeler
    listThisMonth(Users: Users, deadline: string): Observable<Response>{
      let information: Observable<Response>;
      information = this.http.get<Response>(this.baseUrl+"/"+Users.e_posta+"/deadline/"+deadline);
      return information;
    }

    //Idsi verilen id olan kullanıcının bilgilerini şifre ve admin hariç döner
    find(id: Number): Observable<Response>{
        let information: Observable<Response>;
        let body = new FormData();
        body.append("id", ""+id);
        information = this.http.post<Response>(this.baseUrl+"/",body);
        return information;
      }

    //Admin yetkisine sahip kullanıcıları listeler
    listAdmin(Users: Users): Observable<Response>{
        let information: Observable<Response>;
        information = this.http.get<Response>(this.baseUrl+"/"+Users.e_posta+"/admin");
        return information;
    }

    //E-posta adresi verilen kullanıcıyla aynı olan kullanıcıya admin yetkisi verir
    giveAdmin(Users: Users): Observable<Response>{
        let information: Observable<Response>;
        let body = new FormData();
        information = this.http.post<Response>(this.baseUrl+"/"+Users.e_posta+"/authorization", body);
        return information;
      }
    
    //E-posta adresi verilen kullanıcıyla aynı olan kullanıcıdan admin yetkisini alır
    revokeAdmin(Users: Users): Observable<Response>{
        let information: Observable<Response>;
        let body = new FormData();
        information = this.http.post<Response>(this.baseUrl+"/"+Users.e_posta+"/authorization/revoke", body);
        return information;
      }

    //Kullanıcının şifresini verilen şifre olarak günceller
    resetPassword(password: string): Observable<Response>{
        let information: Observable<Response>;
        let body = new FormData();
        body.append("password", password)
        body.append("mail", ""+localStorage.getItem("User_mail"));
        information = this.http.post<Response>(this.baseUrl+"/"+localStorage.getItem("User_mail")+"/reset_password", body);
        return information;
      }

      //E-posta adresi verilen kullanıcıyla aynı olan kullanıcının şifresini verilen şifre yapar
      resetPasswordAdmin(Users: Users, password: string): Observable<Response>{
        let information: Observable<Response>;
        let body = new FormData();
        body.append("password", password)
        body.append("mail", ""+localStorage.getItem("User_mail"));
        information = this.http.post<Response>(this.baseUrl+"/"+Users.e_posta+"/reset_password/admin", body);
        return information;
      }
      
      //Verilen kullanıcının puanına modifier sayısını ekler
      updatePoint(user: Users, modifier: number){
        let information: Observable<Response>;
        let body = new FormData();
        body.append("e_mail", user.e_posta);
        body.append("modifier", ""+modifier);
        information = this.http.post<Response>(this.baseUrl+"/update/point", body);
        return information;
      }

      //Bütün kullanıcıların puanını 0'a set eder
      resetPoints(){
        let information: Observable<Response>;
        let body = new FormData();
        information = this.http.post<Response>(this.baseUrl+"/reset/points", body);
        return information;
      }

      //E-posta adresi verilen kullanıcının aktifliğini değiştirir
      changeActive(e_mail: string){
        let information: Observable<Response>;
        let body = new FormData();
        body.append("e_mail", e_mail);
        information = this.http.post<Response>(this.baseUrl+"/active", body);
        return information;
      }
}