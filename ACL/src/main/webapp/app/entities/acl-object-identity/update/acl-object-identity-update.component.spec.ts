jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AclObjectIdentityService } from '../service/acl-object-identity.service';
import { IAclObjectIdentity, AclObjectIdentity } from '../acl-object-identity.model';
import { IAclClass } from 'app/entities/acl-class/acl-class.model';
import { AclClassService } from 'app/entities/acl-class/service/acl-class.service';

import { AclObjectIdentityUpdateComponent } from './acl-object-identity-update.component';

describe('Component Tests', () => {
  describe('AclObjectIdentity Management Update Component', () => {
    let comp: AclObjectIdentityUpdateComponent;
    let fixture: ComponentFixture<AclObjectIdentityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let aclObjectIdentityService: AclObjectIdentityService;
    let aclClassService: AclClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AclObjectIdentityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AclObjectIdentityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AclObjectIdentityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      aclObjectIdentityService = TestBed.inject(AclObjectIdentityService);
      aclClassService = TestBed.inject(AclClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AclClass query and add missing value', () => {
        const aclObjectIdentity: IAclObjectIdentity = { id: 456 };
        const aclClass: IAclClass = { id: 33003 };
        aclObjectIdentity.aclClass = aclClass;

        const aclClassCollection: IAclClass[] = [{ id: 83738 }];
        jest.spyOn(aclClassService, 'query').mockReturnValue(of(new HttpResponse({ body: aclClassCollection })));
        const additionalAclClasses = [aclClass];
        const expectedCollection: IAclClass[] = [...additionalAclClasses, ...aclClassCollection];
        jest.spyOn(aclClassService, 'addAclClassToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ aclObjectIdentity });
        comp.ngOnInit();

        expect(aclClassService.query).toHaveBeenCalled();
        expect(aclClassService.addAclClassToCollectionIfMissing).toHaveBeenCalledWith(aclClassCollection, ...additionalAclClasses);
        expect(comp.aclClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const aclObjectIdentity: IAclObjectIdentity = { id: 456 };
        const aclClass: IAclClass = { id: 65782 };
        aclObjectIdentity.aclClass = aclClass;

        activatedRoute.data = of({ aclObjectIdentity });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aclObjectIdentity));
        expect(comp.aclClassesSharedCollection).toContain(aclClass);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclObjectIdentity>>();
        const aclObjectIdentity = { id: 123 };
        jest.spyOn(aclObjectIdentityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclObjectIdentity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclObjectIdentity }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(aclObjectIdentityService.update).toHaveBeenCalledWith(aclObjectIdentity);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclObjectIdentity>>();
        const aclObjectIdentity = new AclObjectIdentity();
        jest.spyOn(aclObjectIdentityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclObjectIdentity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclObjectIdentity }));
        saveSubject.complete();

        // THEN
        expect(aclObjectIdentityService.create).toHaveBeenCalledWith(aclObjectIdentity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclObjectIdentity>>();
        const aclObjectIdentity = { id: 123 };
        jest.spyOn(aclObjectIdentityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclObjectIdentity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(aclObjectIdentityService.update).toHaveBeenCalledWith(aclObjectIdentity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAclClassById', () => {
        it('Should return tracked AclClass primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAclClassById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
