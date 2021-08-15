import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'acl-sid',
        data: { pageTitle: 'securityApp.aclSid.home.title' },
        loadChildren: () => import('./acl-sid/acl-sid.module').then(m => m.AclSidModule),
      },
      {
        path: 'acl-class',
        data: { pageTitle: 'securityApp.aclClass.home.title' },
        loadChildren: () => import('./acl-class/acl-class.module').then(m => m.AclClassModule),
      },
      {
        path: 'acl-object-identity',
        data: { pageTitle: 'securityApp.aclObjectIdentity.home.title' },
        loadChildren: () => import('./acl-object-identity/acl-object-identity.module').then(m => m.AclObjectIdentityModule),
      },
      {
        path: 'acl-mask',
        data: { pageTitle: 'securityApp.aclMask.home.title' },
        loadChildren: () => import('./acl-mask/acl-mask.module').then(m => m.AclMaskModule),
      },
      {
        path: 'acl-entry',
        data: { pageTitle: 'securityApp.aclEntry.home.title' },
        loadChildren: () => import('./acl-entry/acl-entry.module').then(m => m.AclEntryModule),
      },
      {
        path: 'tenant',
        data: { pageTitle: 'securityApp.tenant.home.title' },
        loadChildren: () => import('./tenant/tenant.module').then(m => m.TenantModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
