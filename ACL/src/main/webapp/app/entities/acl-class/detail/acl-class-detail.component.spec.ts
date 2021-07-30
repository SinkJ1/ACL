import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AclClassDetailComponent } from './acl-class-detail.component';

describe('Component Tests', () => {
  describe('AclClass Management Detail Component', () => {
    let comp: AclClassDetailComponent;
    let fixture: ComponentFixture<AclClassDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AclClassDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aclClass: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AclClassDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AclClassDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aclClass on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aclClass).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
