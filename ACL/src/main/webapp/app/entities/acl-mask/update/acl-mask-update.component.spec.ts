jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AclMaskService } from '../service/acl-mask.service';
import { IAclMask, AclMask } from '../acl-mask.model';

import { AclMaskUpdateComponent } from './acl-mask-update.component';

describe('Component Tests', () => {
  describe('AclMask Management Update Component', () => {
    let comp: AclMaskUpdateComponent;
    let fixture: ComponentFixture<AclMaskUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let aclMaskService: AclMaskService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AclMaskUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AclMaskUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AclMaskUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      aclMaskService = TestBed.inject(AclMaskService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const aclMask: IAclMask = { id: 456 };

        activatedRoute.data = of({ aclMask });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aclMask));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclMask>>();
        const aclMask = { id: 123 };
        jest.spyOn(aclMaskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclMask });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclMask }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(aclMaskService.update).toHaveBeenCalledWith(aclMask);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclMask>>();
        const aclMask = new AclMask();
        jest.spyOn(aclMaskService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclMask });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclMask }));
        saveSubject.complete();

        // THEN
        expect(aclMaskService.create).toHaveBeenCalledWith(aclMask);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclMask>>();
        const aclMask = { id: 123 };
        jest.spyOn(aclMaskService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclMask });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(aclMaskService.update).toHaveBeenCalledWith(aclMask);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
