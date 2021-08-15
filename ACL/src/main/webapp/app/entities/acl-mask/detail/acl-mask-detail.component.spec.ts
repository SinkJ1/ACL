import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AclMaskDetailComponent } from './acl-mask-detail.component';

describe('Component Tests', () => {
  describe('AclMask Management Detail Component', () => {
    let comp: AclMaskDetailComponent;
    let fixture: ComponentFixture<AclMaskDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AclMaskDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aclMask: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AclMaskDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AclMaskDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aclMask on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aclMask).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
