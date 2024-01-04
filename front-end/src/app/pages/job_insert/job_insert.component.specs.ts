import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobInsertComponent } from './job_insert.component';

describe('JobInsertComponent', () => {
    let component: JobInsertComponent;
    let fixture: ComponentFixture<JobInsertComponent>;
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ JobInsertComponent ]
      })
      .compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(JobInsertComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });