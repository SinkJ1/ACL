import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclSid } from '../acl-sid.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AclSidService } from '../service/acl-sid.service';
import { AclSidDeleteDialogComponent } from '../delete/acl-sid-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { ITenant } from '../../tenant/tenant.model';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-acl-sid',
  templateUrl: './acl-sid.component.html',
})
export class AclSidComponent implements OnInit {
  aclSids: IAclSid[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  headers: any;
  tenants: ITenant[];
  tenantValue: string;

  constructor(
    protected aclSidService: AclSidService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected fb: FormBuilder
  ) {
    this.aclSids = [];
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
    this.loadAll();
    this.tenantValue = '';
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
    this.tenantValue = e.target.value;
    this.aclSids = [];
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
    this.aclSidService
      .query(
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        },
        this.headers
      )
      .subscribe(
        (res: HttpResponse<IAclSid[]>) => {
          this.isLoading = false;
          this.paginateAclSids(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.aclSids = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAclSid): number {
    return item.id!;
  }

  delete(aclSid: IAclSid): void {
    const modalRef = this.modalService.open(AclSidDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aclSid = aclSid;
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

  protected paginateAclSids(data: IAclSid[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.aclSids.push(d);
      }
    }
  }
}
