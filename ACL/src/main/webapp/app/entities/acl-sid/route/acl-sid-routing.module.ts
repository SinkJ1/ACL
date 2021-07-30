import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AclSidComponent } from '../list/acl-sid.component';
import { AclSidDetailComponent } from '../detail/acl-sid-detail.component';
import { AclSidUpdateComponent } from '../update/acl-sid-update.component';
import { AclSidRoutingResolveService } from './acl-sid-routing-resolve.service';

const aclSidRoute: Routes = [
  {
    path: '',
    component: AclSidComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AclSidDetailComponent,
    resolve: {
      aclSid: AclSidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AclSidUpdateComponent,
    resolve: {
      aclSid: AclSidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AclSidUpdateComponent,
    resolve: {
      aclSid: AclSidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aclSidRoute)],
  exports: [RouterModule],
})
export class AclSidRoutingModule {}
