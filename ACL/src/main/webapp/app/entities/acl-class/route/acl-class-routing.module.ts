import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AclClassComponent } from '../list/acl-class.component';
import { AclClassDetailComponent } from '../detail/acl-class-detail.component';
import { AclClassUpdateComponent } from '../update/acl-class-update.component';
import { AclClassRoutingResolveService } from './acl-class-routing-resolve.service';

const aclClassRoute: Routes = [
  {
    path: '',
    component: AclClassComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AclClassDetailComponent,
    resolve: {
      aclClass: AclClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AclClassUpdateComponent,
    resolve: {
      aclClass: AclClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AclClassUpdateComponent,
    resolve: {
      aclClass: AclClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aclClassRoute)],
  exports: [RouterModule],
})
export class AclClassRoutingModule {}
