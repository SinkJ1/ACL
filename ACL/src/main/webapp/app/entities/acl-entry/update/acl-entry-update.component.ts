import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAclEntry, AclEntry } from '../acl-entry.model';
import { AclEntryService } from '../service/acl-entry.service';
import { IAclSid } from 'app/entities/acl-sid/acl-sid.model';
import { AclSidService } from 'app/entities/acl-sid/service/acl-sid.service';
import { IAclObjectIdentity } from 'app/entities/acl-object-identity/acl-object-identity.model';
import { AclObjectIdentityService } from 'app/entities/acl-object-identity/service/acl-object-identity.service';
import { IAclMask } from 'app/entities/acl-mask/acl-mask.model';
import { AclMaskService } from 'app/entities/acl-mask/service/acl-mask.service';

@Component({
  selector: 'jhi-acl-entry-update',
  templateUrl: './acl-entry-update.component.html',
})
export class AclEntryUpdateComponent implements OnInit {
  isSaving = false;

  aclSidsSharedCollection: IAclSid[] = [];
  aclObjectIdentitiesSharedCollection: IAclObjectIdentity[] = [];
  aclMasksSharedCollection: IAclMask[] = [];

  editForm = this.fb.group({
    id: [],
    granting: [],
    aclSid: [],
    aclObjectIdentity: [],
    aclMask: [],
  });

  constructor(
    protected aclEntryService: AclEntryService,
    protected aclSidService: AclSidService,
    protected aclObjectIdentityService: AclObjectIdentityService,
    protected aclMaskService: AclMaskService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclEntry }) => {
      this.updateForm(aclEntry);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aclEntry = this.createFromForm();
    if (aclEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.aclEntryService.update(aclEntry));
    } else {
      this.subscribeToSaveResponse(this.aclEntryService.create(aclEntry));
    }
  }

  trackAclSidById(index: number, item: IAclSid): number {
    return item.id!;
  }

  trackAclObjectIdentityById(index: number, item: IAclObjectIdentity): number {
    return item.id!;
  }

  trackAclMaskById(index: number, item: IAclMask): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAclEntry>>): void {
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

  protected updateForm(aclEntry: IAclEntry): void {
    this.editForm.patchValue({
      id: aclEntry.id,
      granting: aclEntry.granting,
      aclSid: aclEntry.aclSid,
      aclObjectIdentity: aclEntry.aclObjectIdentity,
      aclMask: aclEntry.aclMask,
    });

    this.aclSidsSharedCollection = this.aclSidService.addAclSidToCollectionIfMissing(this.aclSidsSharedCollection, aclEntry.aclSid);
    this.aclObjectIdentitiesSharedCollection = this.aclObjectIdentityService.addAclObjectIdentityToCollectionIfMissing(
      this.aclObjectIdentitiesSharedCollection,
      aclEntry.aclObjectIdentity
    );
    this.aclMasksSharedCollection = this.aclMaskService.addAclMaskToCollectionIfMissing(this.aclMasksSharedCollection, aclEntry.aclMask);
  }

  protected loadRelationshipsOptions(): void {
    this.aclSidService
      .query()
      .pipe(map((res: HttpResponse<IAclSid[]>) => res.body ?? []))
      .pipe(map((aclSids: IAclSid[]) => this.aclSidService.addAclSidToCollectionIfMissing(aclSids, this.editForm.get('aclSid')!.value)))
      .subscribe((aclSids: IAclSid[]) => (this.aclSidsSharedCollection = aclSids));

    this.aclObjectIdentityService
      .query()
      .pipe(map((res: HttpResponse<IAclObjectIdentity[]>) => res.body ?? []))
      .pipe(
        map((aclObjectIdentities: IAclObjectIdentity[]) =>
          this.aclObjectIdentityService.addAclObjectIdentityToCollectionIfMissing(
            aclObjectIdentities,
            this.editForm.get('aclObjectIdentity')!.value
          )
        )
      )
      .subscribe((aclObjectIdentities: IAclObjectIdentity[]) => (this.aclObjectIdentitiesSharedCollection = aclObjectIdentities));

    this.aclMaskService
      .query()
      .pipe(map((res: HttpResponse<IAclMask[]>) => res.body ?? []))
      .pipe(
        map((aclMasks: IAclMask[]) => this.aclMaskService.addAclMaskToCollectionIfMissing(aclMasks, this.editForm.get('aclMask')!.value))
      )
      .subscribe((aclMasks: IAclMask[]) => (this.aclMasksSharedCollection = aclMasks));
  }

  protected createFromForm(): IAclEntry {
    return {
      ...new AclEntry(),
      id: this.editForm.get(['id'])!.value,
      granting: this.editForm.get(['granting'])!.value,
      aclSid: this.editForm.get(['aclSid'])!.value,
      aclObjectIdentity: this.editForm.get(['aclObjectIdentity'])!.value,
      aclMask: this.editForm.get(['aclMask'])!.value,
    };
  }
}
