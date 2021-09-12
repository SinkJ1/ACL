import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAclSid, AclSid } from '../acl-sid.model';
import { AclSidService } from '../service/acl-sid.service';

@Injectable({ providedIn: 'root' })
export class AclSidRoutingResolveService implements Resolve<IAclSid> {
  headers: any;
  constructor(protected service: AclSidService, protected router: Router) {
    this.headers = { 'X-TENANT-ID': sessionStorage.getItem('X-TENANT-ID') };
  }

  resolve(route: ActivatedRouteSnapshot): Observable<IAclSid> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id, this.headers).pipe(
        mergeMap((aclSid: HttpResponse<AclSid>) => {
          if (aclSid.body) {
            return of(aclSid.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AclSid());
  }
}
