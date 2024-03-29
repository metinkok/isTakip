import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllJobsComponent } from './all_jobs.component';

describe('AllJobsComponent', () => {
  let component: AllJobsComponent;
  let fixture: ComponentFixture<AllJobsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllJobsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllJobsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
