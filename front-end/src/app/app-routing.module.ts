import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

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
import { UserComponent } from './pages/user/user.component';
import { AllUsersAdminComponent } from './pages/all_users_admin/all_users_admin.component';
import { AllUsersForAssigning } from './pages/all_users_for_assigning/all_users_for_assigning.component';
import { LogsComponent } from './pages/logs/logs.component';

import { ErrorComponent } from './pages/error/error.component';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: SignUpComponent,
  },
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'home/admin',
    component: HomeAdminComponent,
  },
  {
    path: 'jobs',
    component: AllJobsComponent,
  },
  {
    path: 'jobs/give',
    component: AllJobsAdminComponent,
  },
  {
    path: 'jobs/add',
    component: JobInsertComponent,
  },
  {
    path: 'job/insert/subjob',
    component: InsertSubjobComponent,
  },
  {
    path: 'job',
    component: JobComponent,
  },
  {
    path: 'job/admin',
    component: JobAdminComponent,
  },
  {
    path: 'jobs/inactive',
    component: AllJobsInactiveComponent,
  },
  {
    path: 'users/password/reset',
    component: PasswordReset,
  },
  {
    path: 'user/admin/password_reset',
    component: PasswordResetAdmin,
  },
  {
    path: 'users',
    component: AllUsersComponent,
  },
  {
    path: 'users/admin',
    component: AllUsersAdminComponent,
  },
  {
    path: 'user',
    component: UserComponent,
  },
  {
    path: 'logs',
    component: LogsComponent,
  },
  {
    path: 'assign',
    component: AllUsersForAssigning,
  },
  {
    path: '**',
    component: ErrorComponent,
  }
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }