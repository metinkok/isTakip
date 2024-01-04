import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { AuthenticationInterceptor } from './interceptors/AuthenticationInterceptor';
import { LoginComponent } from './pages/login/login.component';
import { SignUpComponent } from './pages/signup/signup.component';
import { HomeComponent } from './pages/home/home.component';
import { HomeAdminComponent } from './pages/home_admin/home.admin.component';
import { AllJobsComponent } from './pages/all_jobs/all_jobs.component';
import { AllJobsAdminComponent } from './pages/all_jobs_admin/all_jobs_admin.component';
import { AllJobsInactiveComponent } from './pages/all_jobs_inactive/all_jobs_inactive.component';
import { InsertSubjobComponent } from './pages/insert_subjob/insert_subjob.component';
import { JobInsertComponent } from './pages/job_insert/job_insert.component';
import { JobComponent } from './pages/job/job.component';
import { JobAdminComponent } from './pages/job_admin/job_admin.component';
import { PasswordReset } from './pages/password_reset/password_reset.component';
import { PasswordResetAdmin } from './pages/password_reset_admin/password_reset_admin.component';
import { AllUsersComponent } from './pages/all_users/all_users.component';
import { AllUsersAdminComponent } from './pages/all_users_admin/all_users_admin.component';
import { AllUsersForAssigning } from './pages/all_users_for_assigning/all_users_for_assigning.component';
import { UserComponent } from './pages/user/user.component';
import { LogsComponent } from './pages/logs/logs.component';

import { ErrorComponent } from './pages/error/error.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignUpComponent,
    HomeComponent,
    HomeAdminComponent,
    AllJobsComponent,
    AllJobsAdminComponent,
    AllJobsInactiveComponent,
    JobInsertComponent,
    InsertSubjobComponent,
    JobComponent,
    JobAdminComponent,
    PasswordReset,
    PasswordResetAdmin,
    AllUsersComponent,
    AllUsersAdminComponent,
    UserComponent,
    LogsComponent,
    AllUsersForAssigning,

    ErrorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,  
      useClass: AuthenticationInterceptor,  
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
