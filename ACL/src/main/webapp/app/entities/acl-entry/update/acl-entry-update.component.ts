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
  headers: any;
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
  ) {
    this.headers = { 'X-TENANT-ID': sessionStorage.getItem('X-TENANT-ID') };
  }

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
      this.subscribeToSaveResponse(this.aclEntryService.update(aclEntry, this.headers));
    } else {
      this.subscribeToSaveResponse(this.aclEntryService.create(aclEntry, this.headers));
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
      mask: aclEntry.mask,
    });

    this.aclSidsSharedCollection = this.aclSidService.addAclSidToCollectionIfMissing(this.aclSidsSharedCollection, aclEntry.aclSid);
    this.aclObjectIdentitiesSharedCollection = this.aclObjectIdentityService.addAclObjectIdentityToCollectionIfMissing(
      this.aclObjectIdentitiesSharedCollection,
      aclEntry.aclObjectIdentity
    );
    this.aclMasksSharedCollection = this.aclMaskService.addAclMaskToCollectionIfMissing(this.aclMasksSharedCollection, aclEntry.mask);
  }

  protected loadRelationshipsOptions(): void {
    this.aclSidService.query({}, this.headers).subscribe((res: HttpResponse<IAclSid[]>) => {
      this.aclSidsSharedCollection = res.body!;
    });

    this.aclObjectIdentityService.query({}, this.headers).subscribe((res: HttpResponse<IAclObjectIdentity[]>) => {
      this.aclObjectIdentitiesSharedCollection = res.body!;
    });

    this.aclMaskService.query({}, this.headers).subscribe((res: HttpResponse<IAclMask[]>) => {
      this.aclMasksSharedCollection = res.body!;
    });
  }

  protected createFromForm(): IAclEntry {
    return {
      ...new AclEntry(),
      id: this.editForm.get(['id'])!.value,
      granting: true,
      aclSid: this.editForm.get(['aclSid'])!.value,
      aclObjectIdentity: this.editForm.get(['aclObjectIdentity'])!.value,
      mask: this.editForm.get(['aclMask'])!.value,
    };
  }
}
