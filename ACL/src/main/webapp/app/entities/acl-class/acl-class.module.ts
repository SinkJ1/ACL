import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AclClassComponent } from './list/acl-class.component';
import { AclClassDetailComponent } from './detail/acl-class-detail.component';
import { AclClassUpdateComponent } from './update/acl-class-update.component';
import { AclClassDeleteDialogComponent } from './delete/acl-class-delete-dialog.component';
import { AclClassRoutingModule } from './route/acl-class-routing.module';

@NgModule({
  imports: [SharedModule, AclClassRoutingModule],
  declarations: [AclClassComponent, AclClassDetailComponent, AclClassUpdateComponent, AclClassDeleteDialogComponent],
  entryComponents: [AclClassDeleteDialogComponent],
})
export class AclClassModule {}
