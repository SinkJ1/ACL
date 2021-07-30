import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAclEntry } from '../acl-entry.model';

@Component({
  selector: 'jhi-acl-entry-detail',
  templateUrl: './acl-entry-detail.component.html',
})
export class AclEntryDetailComponent implements OnInit {
  aclEntry: IAclEntry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aclEntry }) => {
      this.aclEntry = aclEntry;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
