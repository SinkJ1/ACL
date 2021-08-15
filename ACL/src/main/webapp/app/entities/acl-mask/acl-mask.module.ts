import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AclMaskComponent } from './list/acl-mask.component';
import { AclMaskDetailComponent } from './detail/acl-mask-detail.component';
import { AclMaskUpdateComponent } from './update/acl-mask-update.component';
import { AclMaskDeleteDialogComponent } from './delete/acl-mask-delete-dialog.component';
import { AclMaskRoutingModule } from './route/acl-mask-routing.module';

@NgModule({
  imports: [SharedModule, AclMaskRoutingModule],
  declarations: [AclMaskComponent, AclMaskDetailComponent, AclMaskUpdateComponent, AclMaskDeleteDialogComponent],
  entryComponents: [AclMaskDeleteDialogComponent],
})
export class AclMaskModule {}
