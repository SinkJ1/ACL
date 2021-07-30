import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AclObjectIdentityComponent } from './list/acl-object-identity.component';
import { AclObjectIdentityDetailComponent } from './detail/acl-object-identity-detail.component';
import { AclObjectIdentityUpdateComponent } from './update/acl-object-identity-update.component';
import { AclObjectIdentityDeleteDialogComponent } from './delete/acl-object-identity-delete-dialog.component';
import { AclObjectIdentityRoutingModule } from './route/acl-object-identity-routing.module';

@NgModule({
  imports: [SharedModule, AclObjectIdentityRoutingModule],
  declarations: [
    AclObjectIdentityComponent,
    AclObjectIdentityDetailComponent,
    AclObjectIdentityUpdateComponent,
    AclObjectIdentityDeleteDialogComponent,
  ],
  entryComponents: [AclObjectIdentityDeleteDialogComponent],
})
export class AclObjectIdentityModule {}
