import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclSid } from '../acl-sid.model';
import { AclSidService } from '../service/acl-sid.service';

@Component({
  templateUrl: './acl-sid-delete-dialog.component.html',
})
export class AclSidDeleteDialogComponent {
  aclSid?: IAclSid;
  headers: any;

  constructor(protected aclSidService: AclSidService, protected activeModal: NgbActiveModal) {
    this.headers = { 'X-TENANT-ID': sessionStorage.getItem('X-TENANT-ID') };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclSidService.delete(id, this.headers).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
