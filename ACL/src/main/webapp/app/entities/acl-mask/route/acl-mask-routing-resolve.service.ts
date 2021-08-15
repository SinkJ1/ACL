import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAclMask, AclMask } from '../acl-mask.model';
import { AclMaskService } from '../service/acl-mask.service';

@Injectable({ providedIn: 'root' })
export class AclMaskRoutingResolveService implements Resolve<IAclMask> {
  constructor(protected service: AclMaskService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAclMask> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aclMask: HttpResponse<AclMask>) => {
          if (aclMask.body) {
            return of(aclMask.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AclMask());
  }
}
