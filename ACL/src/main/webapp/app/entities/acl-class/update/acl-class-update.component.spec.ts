jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AclClassService } from '../service/acl-class.service';
import { IAclClass, AclClass } from '../acl-class.model';

import { AclClassUpdateComponent } from './acl-class-update.component';

describe('Component Tests', () => {
  describe('AclClass Management Update Component', () => {
    let comp: AclClassUpdateComponent;
    let fixture: ComponentFixture<AclClassUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let aclClassService: AclClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AclClassUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AclClassUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AclClassUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      aclClassService = TestBed.inject(AclClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const aclClass: IAclClass = { id: 456 };

        activatedRoute.data = of({ aclClass });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aclClass));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclClass>>();
        const aclClass = { id: 123 };
        jest.spyOn(aclClassService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclClass }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(aclClassService.update).toHaveBeenCalledWith(aclClass);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclClass>>();
        const aclClass = new AclClass();
        jest.spyOn(aclClassService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aclClass }));
        saveSubject.complete();

        // THEN
        expect(aclClassService.create).toHaveBeenCalledWith(aclClass);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AclClass>>();
        const aclClass = { id: 123 };
        jest.spyOn(aclClassService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ aclClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(aclClassService.update).toHaveBeenCalledWith(aclClass);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
