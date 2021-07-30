import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AclEntryComponent } from '../list/acl-entry.component';
import { AclEntryDetailComponent } from '../detail/acl-entry-detail.component';
import { AclEntryUpdateComponent } from '../update/acl-entry-update.component';
import { AclEntryRoutingResolveService } from './acl-entry-routing-resolve.service';

const aclEntryRoute: Routes = [
  {
    path: '',
    component: AclEntryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AclEntryDetailComponent,
    resolve: {
      aclEntry: AclEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AclEntryUpdateComponent,
    resolve: {
      aclEntry: AclEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AclEntryUpdateComponent,
    resolve: {
      aclEntry: AclEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aclEntryRoute)],
  exports: [RouterModule],
})
export class AclEntryRoutingModule {}
