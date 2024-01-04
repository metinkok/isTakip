import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InsertSubjobComponent } from './insert_subjob.component';

describe('InsertSubjobComponent', () => {
    let component: InsertSubjobComponent;
    let fixture: ComponentFixture<InsertSubjobComponent>;
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ InsertSubjobComponent ]
      })
      .compileComponents();
    });
  
    beforeEach(() => {
      fixture = TestBed.createComponent(InsertSubjobComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });