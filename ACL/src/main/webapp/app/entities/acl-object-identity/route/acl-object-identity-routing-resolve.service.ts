import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAclObjectIdentity, AclObjectIdentity } from '../acl-object-identity.model';
import { AclObjectIdentityService } from '../service/acl-object-identity.service';

@Injectable({ providedIn: 'root' })
export class AclObjectIdentityRoutingResolveService implements Resolve<IAclObjectIdentity> {
  constructor(protected service: AclObjectIdentityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAclObjectIdentity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aclObjectIdentity: HttpResponse<AclObjectIdentity>) => {
          if (aclObjectIdentity.body) {
            return of(aclObjectIdentity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AclObjectIdentity());
  }
}
