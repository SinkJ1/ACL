jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AclEntryService } from '../service/acl-entry.service';
import { IAclEntry, AclEntry } from '../acl-entry.model';
import { IAclSid } from 'app/entities/acl-sid/acl-sid.model';
import { AclSidService } from 'app/entities/acl-sid/service/acl-sid.service';
import { IAclObjectIdentity } from 'app/entities/acl-object-identity/acl-object-identity.model';
import { AclObjectIdentityService } from 'app/entities/acl-object-identity/service/acl-object-identity.service';
import { IAclMask } from 'app/entities/acl-mask/acl-mask.model';
import { AclMaskService } from 'app/entities/acl-mask/service/acl-mask.service';

import { AclEntryUpdateComponent } from './acl-entry-update.component';

describe('Component Tests', () => {
  describe('AclEntry Management Update Component', () => {
    let comp: AclEntryUpdateComponent;
    let fixture: ComponentFixture<AclEntryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let aclEntryService: AclEntryService;
    let aclSidService: AclSidService;
    let aclObjectIdentityService: AclObjectIdentityService;
    let aclMaskService: AclMaskService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AclEntryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AclEntryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AclEntryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      aclEntryService = TestBed.inject(AclEntryService);
      aclSidService = TestBed.inject(AclSidService);
      aclObjectIdentityService = TestBed.inject(AclObjectIdentityService);
      aclMaskService = TestBed.inject(AclMaskService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AclSid query and add missing value', () => {
        const aclEntry: IAclEntry = { id: 456 };
        const aclSid: IAclSid = { id: 62105 };
        aclEntry.aclSid = aclSid;

        const aclSidCollection: IAclSid[] = [{ id: 38760 }];
        jest.spyOn(aclSidService, 'query').mockReturnValue(of(new HttpResponse({ body: aclSidCollection })));
        const additionalAclSids = [aclSid];
        const expectedCollection: IAclSid[] = [...additionalAclSids, ...aclSidCollection];
        jest.spyOn(aclSidService, 'addAclSidToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ aclEntry });
        comp.ngOnInit();

        expect(aclSidService.query).toHaveBeenCalled();
        expect(aclSidService.addAclSidToCollectionIfMissing).toHaveBeenCalledWith(aclSidCollection, ...additionalAclSids);
        expect(comp.aclSidsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AclObjectIdentity query and add missing value', () => {
        const aclEntry: IAclEntry = { id: 456 };
        const aclObjectIdentity: IAclObjectIdentity = { id: 58607 };
        aclEntry.aclObjectIdentity = aclObjectIdentity;

        const aclObjectIdentityCollection: IAclObjectIdentity[] = [{ id: 15313 }];
        jest.spyOn(aclObjectIdentityService, 'query').mockReturnValue(of(new HttpResponse({ body: aclObjectIdentityCollection })));
        const additionalAclObjectIdentities = [aclObjectIdentity];
        const expectedCollection: IAclObjectIdentity[] = [...additionalAclObjectIdentities, ...aclObjectIdentityCollection];
        jest.spyOn(aclObjectIdentityService, 'addAclObjectIdentityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ aclEntry });
        comp.ngOnInit();

        expect(aclObjectIdentityService.query).toHaveBeenCalled();
        expect(aclObjectIdentityService.addAclObjectIdentityToCollectionIfMissing).toHaveBeenCalledWith(
          aclObjectIdentityCollection,
          ...additionalAclObjectIdentities
        );
        expect(comp.aclObjectIdentitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AclMask query and add missing value', () => {
        const aclEntry: IAclEntry = { id: 456 };
        const aclMask: IAclMask = { id: 50536 };
        aclEntry.aclMask = aclMask;

        const aclMaskCollection: IAclMask[] = [{ id: 71532 }];
        jest.spyOn(aclMaskService, 'query').mockReturnValue(of(new HttpResponse({ body: aclMaskCollection })));
        const additionalAclMasks = [aclMask];
        const expectedCollection: IAclMask[] = [...additionalAclMasks, ...aclMaskCollection];
        jest.spyOn(aclMaskService, 'addAclMaskToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ aclEntry });
        comp.ngOnInit();

        expect(aclMaskService.query).toHaveBeenCalled();
        expect(aclMaskService.addAclMaskToCollectionIfMissing).toHaveBeenCalledWith(aclMaskCollection, ...additionalAclMasks);
        expect(comp.aclMasksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const aclEntry: IAclEntry = { id: 456 };
        const aclSid: IAclSid = { id: 27607 };
        aclEntry.aclSid = aclSid;
        const aclObjectIdentity: IAclObjectIdentity = { id: 6642 };
        aclEntry.aclObjectIdentity = aclObjectIdentity;
        const aclMask: IAclMask = { id: 94526 };
        aclEntry.aclMask = aclMask;

        activatedRoute.data = of({ aclEntry });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aclEntry));
        expect(comp.aclSidsSharedCollection).toContain(aclSid);
        expect(comp.aclObjectIdentitiesSharedCollection).toContain(aclObjectIdentity);
        expect(comp.aclMasksSharedCollection).toContain(aclMask);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclEntry>>();
        const aclEntry = { id: 123 };
        jest.spyOn(aclEntryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclEntry });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclEntry }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(aclEntryService.update).toHaveBeenCalledWith(aclEntry);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclEntry>>();
        const aclEntry = new AclEntry();
        jest.spyOn(aclEntryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclEntry });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclEntry }));
        saveSubject.complete();

        // THEN
        expect(aclEntryService.create).toHaveBeenCalledWith(aclEntry);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclEntry>>();
        const aclEntry = { id: 123 };
        jest.spyOn(aclEntryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclEntry });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(aclEntryService.update).toHaveBeenCalledWith(aclEntry);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAclSidById', () => {
        it('Should return tracked AclSid primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAclSidById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAclObjectIdentityById', () => {
        it('Should return tracked AclObjectIdentity primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAclObjectIdentityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAclMaskById', () => {
        it('Should return tracked AclMask primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAclMaskById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
