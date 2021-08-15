import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAclMask, AclMask } from '../acl-mask.model';
import { AclMaskService } from '../service/acl-mask.service';

@Component({
  selector: 'jhi-acl-mask-update',
  templateUrl: './acl-mask-update.component.html',
})
export class AclMaskUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected aclMaskService: AclMaskService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclMask }) => {
      this.updateForm(aclMask);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aclMask = this.createFromForm();
    if (aclMask.id !== undefined) {
      this.subscribeToSaveResponse(this.aclMaskService.update(aclMask));
    } else {
      this.subscribeToSaveResponse(this.aclMaskService.create(aclMask));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAclMask>>): void {
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

  protected updateForm(aclMask: IAclMask): void {
    this.editForm.patchValue({
      id: aclMask.id,
      name: aclMask.name,
    });
  }

  protected createFromForm(): IAclMask {
    return {
      ...new AclMask(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
