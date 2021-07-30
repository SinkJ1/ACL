import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AclSidComponent } from './list/acl-sid.component';
import { AclSidDetailComponent } from './detail/acl-sid-detail.component';
import { AclSidUpdateComponent } from './update/acl-sid-update.component';
import { AclSidDeleteDialogComponent } from './delete/acl-sid-delete-dialog.component';
import { AclSidRoutingModule } from './route/acl-sid-routing.module';

@NgModule({
  imports: [SharedModule, AclSidRoutingModule],
  declarations: [AclSidComponent, AclSidDetailComponent, AclSidUpdateComponent, AclSidDeleteDialogComponent],
  entryComponents: [AclSidDeleteDialogComponent],
})
export class AclSidModule {}
