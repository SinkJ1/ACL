import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAclClass } from '../acl-class.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AclClassService } from '../service/acl-class.service';
import { AclClassDeleteDialogComponent } from '../delete/acl-class-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { ITenant } from '../../tenant/tenant.model';

@Component({
  selector: 'jhi-acl-class',
  templateUrl: './acl-class.component.html',
})
export class AclClassComponent implements OnInit {
  aclClasses: IAclClass[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  headers: any;
  tenants: ITenant[];

  constructor(protected aclClassService: AclClassService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.aclClasses = [];
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
    this.aclClasses = [];
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

    this.aclClassService
      .query(
        {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        },
        this.headers
      )
      .subscribe(
        (res: HttpResponse<IAclClass[]>) => {
          this.isLoading = false;
          this.paginateAclClasses(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.aclClasses = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAclClass): number {
    return item.id!;
  }

  delete(aclClass: IAclClass): void {
    const modalRef = this.modalService.open(AclClassDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aclClass = aclClass;
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

  protected paginateAclClasses(data: IAclClass[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.aclClasses.push(d);
      }
    }
  }
}
