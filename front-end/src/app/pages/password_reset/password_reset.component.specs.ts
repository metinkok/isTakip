import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordReset } from './password_reset.component';

describe('PasswordReset', () => {
    let component: PasswordReset;
    let fixture: ComponentFixture<PasswordReset>;
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ PasswordReset ]
      })
      .compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(PasswordReset);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });