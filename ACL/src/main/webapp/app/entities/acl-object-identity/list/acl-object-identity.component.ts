import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclObjectIdentity } from '../acl-object-identity.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AclObjectIdentityService } from '../service/acl-object-identity.service';
import { AclObjectIdentityDeleteDialogComponent } from '../delete/acl-object-identity-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { ITenant } from '../../tenant/tenant.model';

@Component({
  selector: 'jhi-acl-object-identity',
  templateUrl: './acl-object-identity.component.html',
})
export class AclObjectIdentityComponent implements OnInit {
  aclObjectIdentities: IAclObjectIdentity[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  headers: any;
  tenants: ITenant[];

  constructor(
    protected aclObjectIdentityService: AclObjectIdentityService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.aclObjectIdentities = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.headers = { 'X-TENANT-ID': 'public' };
    this.tenants = [];
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
    this.aclObjectIdentities = [];
    this.loadAll();
    sessionStorage.setItem('X-TENANT-ID', e.target.value);
  }

  loadTeanants(): void {
    this.getData('https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/get-all-tenants').then(data => {
      this.tenants = data;
    });
  }

  loadAll(): void {
    this.isLoading = true;

    this.aclObjectIdentityService
      .query(
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        },
        this.headers
      )
      .subscribe(
        (res: HttpResponse<IAclObjectIdentity[]>) => {
          this.isLoading = false;
          this.paginateAclObjectIdentities(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.aclObjectIdentities = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAclObjectIdentity): number {
    return item.id!;
  }

  delete(aclObjectIdentity: IAclObjectIdentity): void {
    const modalRef = this.modalService.open(AclObjectIdentityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aclObjectIdentity = aclObjectIdentity;
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

  protected paginateAclObjectIdentities(data: IAclObjectIdentity[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.aclObjectIdentities.push(d);
      }
    }
  }
}
