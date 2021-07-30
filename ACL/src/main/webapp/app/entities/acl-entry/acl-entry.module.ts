import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AclEntryComponent } from './list/acl-entry.component';
import { AclEntryDetailComponent } from './detail/acl-entry-detail.component';
import { AclEntryUpdateComponent } from './update/acl-entry-update.component';
import { AclEntryDeleteDialogComponent } from './delete/acl-entry-delete-dialog.component';
import { AclEntryRoutingModule } from './route/acl-entry-routing.module';

@NgModule({
  imports: [SharedModule, AclEntryRoutingModule],
  declarations: [AclEntryComponent, AclEntryDetailComponent, AclEntryUpdateComponent, AclEntryDeleteDialogComponent],
  entryComponents: [AclEntryDeleteDialogComponent],
})
export class AclEntryModule {}
