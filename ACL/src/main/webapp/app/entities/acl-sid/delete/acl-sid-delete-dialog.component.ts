import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclSid } from '../acl-sid.model';
import { AclSidService } from '../service/acl-sid.service';

@Component({
  templateUrl: './acl-sid-delete-dialog.component.html',
})
export class AclSidDeleteDialogComponent {
  aclSid?: IAclSid;

  constructor(protected aclSidService: AclSidService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclSidService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
