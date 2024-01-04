import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobAdminComponent } from './job_admin.component';

describe('JobComponent', () => {
    let component: JobAdminComponent;
    let fixture: ComponentFixture<JobAdminComponent>;
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ JobAdminComponent ]
      })
      .compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(JobAdminComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });