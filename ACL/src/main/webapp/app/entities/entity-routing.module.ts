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
        path: 'acl-entry',
        data: { pageTitle: 'securityApp.aclEntry.home.title' },
        loadChildren: () => import('./acl-entry/acl-entry.module').then(m => m.AclEntryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
