import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAclMask } from '../acl-mask.model';

@Component({
  selector: 'jhi-acl-mask-detail',
  templateUrl: './acl-mask-detail.component.html',
})
export class AclMaskDetailComponent implements OnInit {
  aclMask: IAclMask | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclMask }) => {
      this.aclMask = aclMask;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
