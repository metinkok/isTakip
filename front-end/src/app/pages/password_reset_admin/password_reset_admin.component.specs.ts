import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordResetAdmin } from './password_reset_admin.component';

describe('PasswordReset', () => {
    let component: PasswordResetAdmin;
    let fixture: ComponentFixture<PasswordResetAdmin>;
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ PasswordResetAdmin ]
      })
      .compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(PasswordResetAdmin);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });