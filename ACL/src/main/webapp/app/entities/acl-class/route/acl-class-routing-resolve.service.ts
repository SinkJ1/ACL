import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAclClass, AclClass } from '../acl-class.model';
import { AclClassService } from '../service/acl-class.service';

@Injectable({ providedIn: 'root' })
export class AclClassRoutingResolveService implements Resolve<IAclClass> {
  constructor(protected service: AclClassService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAclClass> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aclClass: HttpResponse<AclClass>) => {
          if (aclClass.body) {
            return of(aclClass.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AclClass());
  }
}
