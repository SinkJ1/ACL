import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAclSid } from '../acl-sid.model';

@Component({
  selector: 'jhi-acl-sid-detail',
  templateUrl: './acl-sid-detail.component.html',
})
export class AclSidDetailComponent implements OnInit {
  aclSid: IAclSid | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclSid }) => {
      this.aclSid = aclSid;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
