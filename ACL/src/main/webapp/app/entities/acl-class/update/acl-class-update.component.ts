import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAclClass, AclClass } from '../acl-class.model';
import { AclClassService } from '../service/acl-class.service';

@Component({
  selector: 'jhi-acl-class-update',
  templateUrl: './acl-class-update.component.html',
})
export class AclClassUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    className: [null, [Validators.required]],
    classIdType: [null, [Validators.required]],
  });

  constructor(protected aclClassService: AclClassService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclClass }) => {
      this.updateForm(aclClass);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aclClass = this.createFromForm();
    if (aclClass.id !== undefined) {
      this.subscribeToSaveResponse(this.aclClassService.update(aclClass));
    } else {
      this.subscribeToSaveResponse(this.aclClassService.create(aclClass));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAclClass>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(aclClass: IAclClass): void {
    this.editForm.patchValue({
      id: aclClass.id,
      className: aclClass.className,
      classIdType: aclClass.classIdType,
    });
  }

  protected createFromForm(): IAclClass {
    return {
      ...new AclClass(),
      id: this.editForm.get(['id'])!.value,
      className: this.editForm.get(['className'])!.value,
      classIdType: this.editForm.get(['classIdType'])!.value,
    };
  }
}
