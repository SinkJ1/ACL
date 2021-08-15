import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclMask } from '../acl-mask.model';
import { AclMaskService } from '../service/acl-mask.service';

@Component({
  templateUrl: './acl-mask-delete-dialog.component.html',
})
export class AclMaskDeleteDialogComponent {
  aclMask?: IAclMask;

  constructor(protected aclMaskService: AclMaskService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclMaskService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
