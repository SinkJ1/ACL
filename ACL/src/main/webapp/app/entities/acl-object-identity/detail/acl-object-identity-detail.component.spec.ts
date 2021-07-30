import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AclObjectIdentityDetailComponent } from './acl-object-identity-detail.component';

describe('Component Tests', () => {
  describe('AclObjectIdentity Management Detail Component', () => {
    let comp: AclObjectIdentityDetailComponent;
    let fixture: ComponentFixture<AclObjectIdentityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AclObjectIdentityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aclObjectIdentity: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AclObjectIdentityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AclObjectIdentityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aclObjectIdentity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aclObjectIdentity).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
