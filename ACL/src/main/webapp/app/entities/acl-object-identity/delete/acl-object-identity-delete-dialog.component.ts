import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclObjectIdentity } from '../acl-object-identity.model';
import { AclObjectIdentityService } from '../service/acl-object-identity.service';

@Component({
  templateUrl: './acl-object-identity-delete-dialog.component.html',
})
export class AclObjectIdentityDeleteDialogComponent {
  aclObjectIdentity?: IAclObjectIdentity;
  headers: any;
  constructor(protected aclObjectIdentityService: AclObjectIdentityService, protected activeModal: NgbActiveModal) {
    this.headers = { 'X-TENANT-ID': sessionStorage.getItem('X-TENANT-ID') };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclObjectIdentityService.delete(id, this.headers).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
