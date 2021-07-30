import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AclObjectIdentityComponent } from '../list/acl-object-identity.component';
import { AclObjectIdentityDetailComponent } from '../detail/acl-object-identity-detail.component';
import { AclObjectIdentityUpdateComponent } from '../update/acl-object-identity-update.component';
import { AclObjectIdentityRoutingResolveService } from './acl-object-identity-routing-resolve.service';

const aclObjectIdentityRoute: Routes = [
  {
    path: '',
    component: AclObjectIdentityComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AclObjectIdentityDetailComponent,
    resolve: {
      aclObjectIdentity: AclObjectIdentityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AclObjectIdentityUpdateComponent,
    resolve: {
      aclObjectIdentity: AclObjectIdentityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AclObjectIdentityUpdateComponent,
    resolve: {
      aclObjectIdentity: AclObjectIdentityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aclObjectIdentityRoute)],
  exports: [RouterModule],
})
export class AclObjectIdentityRoutingModule {}
