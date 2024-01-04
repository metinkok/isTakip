import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllJobsAdminComponent } from './all_jobs_admin.component';

describe('AllJobsAdminComponent', () => {
  let component: AllJobsAdminComponent;
  let fixture: ComponentFixture<AllJobsAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllJobsAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllJobsAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
