import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclEntry } from '../acl-entry.model';
import { AclEntryService } from '../service/acl-entry.service';

@Component({
  templateUrl: './acl-entry-delete-dialog.component.html',
})
export class AclEntryDeleteDialogComponent {
  aclEntry?: IAclEntry;

  constructor(protected aclEntryService: AclEntryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclEntryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
