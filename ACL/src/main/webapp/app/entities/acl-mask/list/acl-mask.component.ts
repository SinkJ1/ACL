import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITenant } from '../../tenant/tenant.model';
import { IAclMask } from '../acl-mask.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AclMaskService } from '../service/acl-mask.service';
import { AclMaskDeleteDialogComponent } from '../delete/acl-mask-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-acl-mask',
  templateUrl: './acl-mask.component.html',
})
export class AclMaskComponent implements OnInit {
  aclMasks: IAclMask[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  headers: any;
  tenants: ITenant[];

  constructor(protected aclMaskService: AclMaskService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.aclMasks = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.headers = { 'X-TENANT-ID': 'public' };
    this.ascending = true;
    this.tenants = [];
    this.loadTeanants();
  }

  loadAll(): void {
    this.isLoading = true;

    this.aclMaskService
      .query(
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        },
        this.headers
      )
      .subscribe(
        (res: HttpResponse<IAclMask[]>) => {
          this.isLoading = false;
          this.paginateAclMasks(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
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
    this.aclMasks = [];
    this.loadAll();
    sessionStorage.setItem('X-TENANT-ID', e.target.value);
  }

  loadTeanants(): void {
    this.getData('https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/get-all-tenants').then(data => {
      this.tenants = data;
    });
  }

  reset(): void {
    this.page = 0;
    this.aclMasks = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAclMask): number {
    return item.id!;
  }

  delete(aclMask: IAclMask): void {
    const modalRef = this.modalService.open(AclMaskDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aclMask = aclMask;
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

  protected paginateAclMasks(data: IAclMask[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.aclMasks.push(d);
      }
    }
  }
}
