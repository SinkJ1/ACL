import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclEntry } from '../acl-entry.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AclEntryService } from '../service/acl-entry.service';
import { AclEntryDeleteDialogComponent } from '../delete/acl-entry-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { ITenant } from '../../tenant/tenant.model';

@Component({
  selector: 'jhi-acl-entry',
  templateUrl: './acl-entry.component.html',
})
export class AclEntryComponent implements OnInit {
  aclEntries: IAclEntry[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  headers: any;
  tenants: ITenant[];

  constructor(protected aclEntryService: AclEntryService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.aclEntries = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.tenants = [];
    this.headers = { 'X-TENANT-ID': 'public' };
    this.loadTeanants();
  }

  async getData(url = ''): Promise<ITenant[]> {
    const check = sessionStorage.getItem('jhi-authenticationToken');
    let token = `Bearer ${check ? check : ''}`;
    token = token.replace('"', '');
    token = token.replace('"', '');
    const response: any = await fetch(url, {
      method: 'GET',
      headers: {
        Authorization: token,
      },
    });

    const data: ITenant[] = await response.json();
    return data;
  }

  changeShema(e: any): any {
    this.headers = { 'X-TENANT-ID': e.target.value };
    this.aclEntries = [];
    this.loadAll();
    sessionStorage.setItem('X-TENANT-ID', e.target.value);
  }

  loadTeanants(): void {
    this.getData('http://localhost:8080/api/get-all-tenants').then(data => {
      this.tenants = data;
    });
  }

  loadAll(): void {
    this.isLoading = true;

    this.aclEntryService
      .query(
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        },
        this.headers
      )
      .subscribe(
        (res: HttpResponse<IAclEntry[]>) => {
          this.isLoading = false;
          this.paginateAclEntries(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.aclEntries = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAclEntry): number {
    return item.id!;
  }

  delete(aclEntry: IAclEntry): void {
    const modalRef = this.modalService.open(AclEntryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aclEntry = aclEntry;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAclEntries(data: IAclEntry[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      console.log(data);
      for (const d of data) {
        this.aclEntries.push(d);
      }
    }
  }
}
