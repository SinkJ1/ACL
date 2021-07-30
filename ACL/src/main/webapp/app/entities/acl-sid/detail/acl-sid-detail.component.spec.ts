import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AclSidDetailComponent } from './acl-sid-detail.component';

describe('Component Tests', () => {
  describe('AclSid Management Detail Component', () => {
    let comp: AclSidDetailComponent;
    let fixture: ComponentFixture<AclSidDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AclSidDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aclSid: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AclSidDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AclSidDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aclSid on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aclSid).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
