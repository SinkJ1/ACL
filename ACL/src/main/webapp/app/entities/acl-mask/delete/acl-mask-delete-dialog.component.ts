import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclMask } from '../acl-mask.model';
import { AclMaskService } from '../service/acl-mask.service';

@Component({
  templateUrl: './acl-mask-delete-dialog.component.html',
})
export class AclMaskDeleteDialogComponent {
  aclMask?: IAclMask;
  headers: any;
  constructor(protected aclMaskService: AclMaskService, protected activeModal: NgbActiveModal) {
    this.headers = { 'X-TENANT-ID': sessionStorage.getItem('X-TENANT-ID') };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclMaskService.delete(id, this.headers).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
