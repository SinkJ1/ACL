import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclEntry } from '../acl-entry.model';
import { AclEntryService } from '../service/acl-entry.service';

@Component({
  templateUrl: './acl-entry-delete-dialog.component.html',
})
export class AclEntryDeleteDialogComponent {
  aclEntry?: IAclEntry;
  headers: any;
  constructor(protected aclEntryService: AclEntryService, protected activeModal: NgbActiveModal) {
    this.headers = { 'X-TENANT-ID': sessionStorage.getItem('X-TENANT-ID') };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclEntryService.delete(id, this.headers).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
