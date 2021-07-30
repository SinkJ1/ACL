import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAclEntry, AclEntry } from '../acl-entry.model';
import { AclEntryService } from '../service/acl-entry.service';

@Injectable({ providedIn: 'root' })
export class AclEntryRoutingResolveService implements Resolve<IAclEntry> {
  constructor(protected service: AclEntryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAclEntry> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aclEntry: HttpResponse<AclEntry>) => {
          if (aclEntry.body) {
            return of(aclEntry.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AclEntry());
  }
}
