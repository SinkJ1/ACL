import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAclClass } from '../acl-class.model';

@Component({
  selector: 'jhi-acl-class-detail',
  templateUrl: './acl-class-detail.component.html',
})
export class AclClassDetailComponent implements OnInit {
  aclClass: IAclClass | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclClass }) => {
      this.aclClass = aclClass;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
