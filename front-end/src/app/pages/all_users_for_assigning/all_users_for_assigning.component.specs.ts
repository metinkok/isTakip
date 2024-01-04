import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllUsersForAssigning } from './all_users_for_assigning.component';

describe('AllUsersForAssigning', () => {
  let component: AllUsersForAssigning;
  let fixture: ComponentFixture<AllUsersForAssigning>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllUsersForAssigning ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllUsersForAssigning);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
