import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AclEntryDetailComponent } from './acl-entry-detail.component';

describe('Component Tests', () => {
  describe('AclEntry Management Detail Component', () => {
    let comp: AclEntryDetailComponent;
    let fixture: ComponentFixture<AclEntryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AclEntryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aclEntry: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AclEntryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AclEntryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aclEntry on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aclEntry).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
