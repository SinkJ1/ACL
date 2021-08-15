import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAclSid, AclSid } from '../acl-sid.model';
import { AclSidService } from '../service/acl-sid.service';

@Component({
  selector: 'jhi-acl-sid-update',
  templateUrl: './acl-sid-update.component.html',
})
export class AclSidUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sid: [null, [Validators.required]],
  });

  constructor(protected aclSidService: AclSidService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclSid }) => {
      this.updateForm(aclSid);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aclSid = this.createFromForm();
    if (aclSid.id !== undefined) {
      this.subscribeToSaveResponse(this.aclSidService.update(aclSid));
    } else {
      this.subscribeToSaveResponse(this.aclSidService.create(aclSid));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAclSid>>): void {
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

  protected updateForm(aclSid: IAclSid): void {
    this.editForm.patchValue({
      id: aclSid.id,
      sid: aclSid.sid,
    });
  }

  protected createFromForm(): IAclSid {
    return {
      ...new AclSid(),
      id: this.editForm.get(['id'])!.value,
      sid: this.editForm.get(['sid'])!.value,
    };
  }
}
