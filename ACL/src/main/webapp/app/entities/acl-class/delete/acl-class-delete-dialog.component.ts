import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclClass } from '../acl-class.model';
import { AclClassService } from '../service/acl-class.service';

@Component({
  templateUrl: './acl-class-delete-dialog.component.html',
})
export class AclClassDeleteDialogComponent {
  aclClass?: IAclClass;

  constructor(protected aclClassService: AclClassService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclClassService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
