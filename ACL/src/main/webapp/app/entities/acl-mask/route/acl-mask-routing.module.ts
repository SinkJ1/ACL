import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AclMaskComponent } from '../list/acl-mask.component';
import { AclMaskDetailComponent } from '../detail/acl-mask-detail.component';
import { AclMaskUpdateComponent } from '../update/acl-mask-update.component';
import { AclMaskRoutingResolveService } from './acl-mask-routing-resolve.service';

const aclMaskRoute: Routes = [
  {
    path: '',
    component: AclMaskComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AclMaskDetailComponent,
    resolve: {
      aclMask: AclMaskRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AclMaskUpdateComponent,
    resolve: {
      aclMask: AclMaskRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AclMaskUpdateComponent,
    resolve: {
      aclMask: AclMaskRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aclMaskRoute)],
  exports: [RouterModule],
})
export class AclMaskRoutingModule {}
