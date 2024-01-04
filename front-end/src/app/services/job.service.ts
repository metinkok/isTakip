import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Jobs } from '../exports/Jobs';
import { Users } from '../exports/Users';
import { Response } from '../exports/Response';
import { Service } from './service';

@Injectable({
  providedIn: 'root'
})
export class JobService {
    private baseUrl = this.Service.getUrl()+"jobs";
    constructor(private http: HttpClient, private Service : Service) { }

  //Bütün inaktif olmayan işleri listeler
  list(): Observable<Response>{
    let information: Observable<Response>;
    information = this.http.get<Response>(this.baseUrl+"/");
    return information;
  }

  //Bütün işleri listeler
  listInactive(): Observable<Response>{
    let information: Observable<Response>;
    information = this.http.get<Response>(this.baseUrl+"/inactive");
    return information;
  }

  //Idsi verilen işin bütün alt işlerini listeler
  listSubJobs(parent: number): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("parent", ""+parent)
    information = this.http.post<Response>(this.baseUrl+"/subjobs", body);
    return information;
  }

  //Verilen statüdeki işleri listeler
  listWithStatus(status:number): Observable<Response>{
    let information: Observable<Response>;
    information = this.http.get<Response>(this.baseUrl+"/status/"+status);
    return information;
  }

  //Verilen deadline'ın ayındaki işleri listeler
  listThisMonth(deadline:string): Observable<Response>{
    let information: Observable<Response>;
    information = this.http.get<Response>(this.baseUrl+"/deadline/month/"+deadline);
    return information;
  }

  //Idsi verilen işin bilgilerini döner
  listOne(Jobs: Jobs): Observable<Response>{
    let information: Observable<Response>;
    if(Jobs.id == null)
      Jobs.id = -1;
    information = this.http.get<Response>(this.baseUrl+"/"+Jobs.id);
    return information;
  }

  //Deadline'ı verilen gün olan işleri listeler
  listToday(today:String): Observable<Response>{
    let information: Observable<Response>;
    information = this.http.get<Response>(this.baseUrl+"/today/"+today);
    return information;
  }

  //Deadline'ı verilen tarihte ve verilen tarihten sonra olan işleri listeler
  listNotEnded(today:String): Observable<Response>{
    let information: Observable<Response>;
    information = this.http.get<Response>(this.baseUrl+"/deadline/"+today);
    return information;
  }

  //İsminde verilen substring geçen işleri listeler
  search(name: string): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("name", name)
    information = this.http.post<Response>(this.baseUrl+"/search/name",body);
    return information;
  }
  
  //Verilen işi veritabanına ekler
  insert(Jobs: Jobs): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("name", Jobs.isim);
    body.append("expl", Jobs.aciklama);
    body.append("point", ""+Jobs.point);
    body.append("status", ""+Jobs.status);
    body.append("deadline", ""+Jobs.deadline);
    information = this.http.post<Response>(this.baseUrl+"/insert", body);
    return information;
  }

  //Verilen alt işi veritabanına ekler
  insertSubJob(Jobs: Jobs): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("name", Jobs.isim);
    body.append("expl", Jobs.aciklama);
    body.append("point", ""+Jobs.point);
    body.append("status", ""+Jobs.status);
    body.append("deadline", ""+Jobs.deadline);
    body.append("parent", ""+Jobs.parent);
    information = this.http.post<Response>(this.baseUrl+"/subjob/insert", body);
    return information;
  }

  //Idsi verilen işle aynı olan işin deadline'ını verilen işin deadline'ı olarak set eder
  updateDeadline(Jobs: Jobs): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("id", ""+Jobs.id);
    body.append("deadline", ""+Jobs.deadline);
    information = this.http.post<Response>(this.baseUrl+"/deadline/update", body);
    return information;
  }

  //Idsi verilen işle aynı olan işi kullanıcıya atar
  take(Jobs: Jobs): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("e_mail", ""+localStorage.getItem("User_mail"));
    information = this.http.post<Response>(this.baseUrl+"/"+Jobs.id+"/take", body);
    return information;
  }

  //Idsi verilen işle aynı olan işi Idsi verilen kullanıcıyla aynı olan kulllanıcıya atar
  give(Jobs: Jobs, Users: Users): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("user_id", ""+Users.id);
    body.append("e_mail", ""+localStorage.getItem("User_mail"));
    information = this.http.post<Response>(this.baseUrl+"/"+Jobs.id+"/give", body);
    return information;
  }

  //Idsi verilen işle aynı olan işi kullanıcıdan alır
  drop(Jobs: Jobs): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("e_mail", ""+localStorage.getItem("User_mail"));
    information = this.http.post<Response>(this.baseUrl+"/"+Jobs.id+"/withdraw", body);
    return information;
  }

  //Idsi verilen işle aynı olan işin statüsünü verilen işin statüsüne set eder
  statusChange(Jobs: Jobs): Observable<Response>{
    let information: Observable<Response>;
    let body = new FormData();
    information = this.http.post<Response>(this.baseUrl+"/"+Jobs.id+"/"+Jobs.status, body);
    return information;
  }

  //Idsi veriilen işle aynı olan işi tüm adminlere atar
  giveJobToAdmins(job: Jobs): Observable<any>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("job_id", ""+job.id);
    information = this.http.post<Response>("http://localhost:8080/api/admins", body);
    return information;
    }

  //Idsi verilen işle aynı olan işi üzerinde tutan kullanıcıları listeler
  usersInJob(job: Jobs): Observable<any>{
    let information: Observable<Response>;
    information = this.http.get<Response>(this.baseUrl+"/"+job.id+"/users");
    return information;
    }

  //Idsi verilen işin puanını günceller
  updatePoint(job: Jobs): Observable<any>{
    let information: Observable<Response>;
    let body = new FormData();
    body.append("point", ""+job.point);
    information = this.http.post<Response>(this.baseUrl+"/"+job.id+"/point/update", body);
    return information;
    }
}

