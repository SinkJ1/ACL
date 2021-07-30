jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AclSidService } from '../service/acl-sid.service';
import { IAclSid, AclSid } from '../acl-sid.model';

import { AclSidUpdateComponent } from './acl-sid-update.component';

describe('Component Tests', () => {
  describe('AclSid Management Update Component', () => {
    let comp: AclSidUpdateComponent;
    let fixture: ComponentFixture<AclSidUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let aclSidService: AclSidService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AclSidUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AclSidUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AclSidUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      aclSidService = TestBed.inject(AclSidService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const aclSid: IAclSid = { id: 456 };

        activatedRoute.data = of({ aclSid });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aclSid));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclSid>>();
        const aclSid = { id: 123 };
        jest.spyOn(aclSidService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclSid });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclSid }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(aclSidService.update).toHaveBeenCalledWith(aclSid);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclSid>>();
        const aclSid = new AclSid();
        jest.spyOn(aclSidService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclSid });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclSid }));
        saveSubject.complete();

        // THEN
        expect(aclSidService.create).toHaveBeenCalledWith(aclSid);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclSid>>();
        const aclSid = { id: 123 };
        jest.spyOn(aclSidService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclSid });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(aclSidService.update).toHaveBeenCalledWith(aclSid);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
