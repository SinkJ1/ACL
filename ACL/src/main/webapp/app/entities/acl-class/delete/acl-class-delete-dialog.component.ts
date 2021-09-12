import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclClass } from '../acl-class.model';
import { AclClassService } from '../service/acl-class.service';

@Component({
  templateUrl: './acl-class-delete-dialog.component.html',
})
export class AclClassDeleteDialogComponent {
  aclClass?: IAclClass;
  headers: any;
  constructor(protected aclClassService: AclClassService, protected activeModal: NgbActiveModal) {
    this.headers = { 'X-TENANT-ID': sessionStorage.getItem('X-TENANT-ID') };
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aclClassService.delete(id, this.headers).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
