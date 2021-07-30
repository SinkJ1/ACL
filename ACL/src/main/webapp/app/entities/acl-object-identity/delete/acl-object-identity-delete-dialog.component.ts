import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclObjectIdentity } from '../acl-object-identity.model';
import { AclObjectIdentityService } from '../service/acl-object-identity.service';

@Component({
  templateUrl: './acl-object-identity-delete-dialog.component.html',
})
export class AclObjectIdentityDeleteDialogComponent {
  aclObjectIdentity?: IAclObjectIdentity;

  constructor(protected aclObjectIdentityService: AclObjectIdentityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclObjectIdentityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
