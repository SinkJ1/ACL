import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAclObjectIdentity } from '../acl-object-identity.model';

@Component({
  selector: 'jhi-acl-object-identity-detail',
  templateUrl: './acl-object-identity-detail.component.html',
})
export class AclObjectIdentityDetailComponent implements OnInit {
  aclObjectIdentity: IAclObjectIdentity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclObjectIdentity }) => {
      this.aclObjectIdentity = aclObjectIdentity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
