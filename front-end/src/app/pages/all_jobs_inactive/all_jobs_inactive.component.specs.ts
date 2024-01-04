import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllJobsInactiveComponent } from './all_jobs_inactive.component';

describe('AllJobsInactive', () => {
  let component: AllJobsInactiveComponent;
  let fixture: ComponentFixture<AllJobsInactiveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllJobsInactiveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllJobsInactiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
