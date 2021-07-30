import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAclObjectIdentity, AclObjectIdentity } from '../acl-object-identity.model';
import { AclObjectIdentityService } from '../service/acl-object-identity.service';
import { IAclClass } from 'app/entities/acl-class/acl-class.model';
import { AclClassService } from 'app/entities/acl-class/service/acl-class.service';

@Component({
  selector: 'jhi-acl-object-identity-update',
  templateUrl: './acl-object-identity-update.component.html',
})
export class AclObjectIdentityUpdateComponent implements OnInit {
  isSaving = false;

  aclClassesSharedCollection: IAclClass[] = [];

  editForm = this.fb.group({
    id: [],
    objectIdIdentity: [null, [Validators.required]],
    parentObject: [null, [Validators.required]],
    ownerSid: [],
    entriesInheriting: [],
    aclClass: [],
  });

  constructor(
    protected aclObjectIdentityService: AclObjectIdentityService,
    protected aclClassService: AclClassService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclObjectIdentity }) => {
      this.updateForm(aclObjectIdentity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aclObjectIdentity = this.createFromForm();
    if (aclObjectIdentity.id !== undefined) {
      this.subscribeToSaveResponse(this.aclObjectIdentityService.update(aclObjectIdentity));
    } else {
      this.subscribeToSaveResponse(this.aclObjectIdentityService.create(aclObjectIdentity));
    }
  }

  trackAclClassById(index: number, item: IAclClass): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAclObjectIdentity>>): void {
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

  protected updateForm(aclObjectIdentity: IAclObjectIdentity): void {
    this.editForm.patchValue({
      id: aclObjectIdentity.id,
      objectIdIdentity: aclObjectIdentity.objectIdIdentity,
      parentObject: aclObjectIdentity.parentObject,
      ownerSid: aclObjectIdentity.ownerSid,
      entriesInheriting: aclObjectIdentity.entriesInheriting,
      aclClass: aclObjectIdentity.aclClass,
    });

    this.aclClassesSharedCollection = this.aclClassService.addAclClassToCollectionIfMissing(
      this.aclClassesSharedCollection,
      aclObjectIdentity.aclClass
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aclClassService
      .query()
      .pipe(map((res: HttpResponse<IAclClass[]>) => res.body ?? []))
      .pipe(
        map((aclClasses: IAclClass[]) =>
          this.aclClassService.addAclClassToCollectionIfMissing(aclClasses, this.editForm.get('aclClass')!.value)
        )
      )
      .subscribe((aclClasses: IAclClass[]) => (this.aclClassesSharedCollection = aclClasses));
  }

  protected createFromForm(): IAclObjectIdentity {
    return {
      ...new AclObjectIdentity(),
      id: this.editForm.get(['id'])!.value,
      objectIdIdentity: this.editForm.get(['objectIdIdentity'])!.value,
      parentObject: this.editForm.get(['parentObject'])!.value,
      ownerSid: this.editForm.get(['ownerSid'])!.value,
      entriesInheriting: this.editForm.get(['entriesInheriting'])!.value,
      aclClass: this.editForm.get(['aclClass'])!.value,
    };
  }
}
