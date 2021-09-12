import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAclClass, getAclClassIdentifier } from '../acl-class.model';

export type EntityResponseType = HttpResponse<IAclClass>;
export type EntityArrayResponseType = HttpResponse<IAclClass[]>;

@Injectable({ providedIn: 'root' })
export class AclClassService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acl-classes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aclClass: IAclClass, headers: any): Observable<EntityResponseType> {
    return this.http.post<IAclClass>(this.resourceUrl, aclClass, { observe: 'response', headers });
  }

  update(aclClass: IAclClass, headers: any): Observable<EntityResponseType> {
    return this.http.put<IAclClass>(`${this.resourceUrl}/${getAclClassIdentifier(aclClass) as number}`, aclClass, {
      observe: 'response',
      headers,
    });
  }

  partialUpdate(aclClass: IAclClass, headers: any): Observable<EntityResponseType> {
    return this.http.patch<IAclClass>(`${this.resourceUrl}/${getAclClassIdentifier(aclClass) as number}`, aclClass, {
      observe: 'response',
      headers,
    });
  }

  find(id: number, headers: any): Observable<EntityResponseType> {
    return this.http.get<IAclClass>(`${this.resourceUrl}/${id}`, { observe: 'response', headers });
  }

  query(req?: any, headers?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAclClass[]>(this.resourceUrl, { params: options, observe: 'response', headers });
  }

  delete(id: number, headers: any): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response', headers });
  }

  addAclClassToCollectionIfMissing(aclClassCollection: IAclClass[], ...aclClassesToCheck: (IAclClass | null | undefined)[]): IAclClass[] {
    const aclClasses: IAclClass[] = aclClassesToCheck.filter(isPresent);
    if (aclClasses.length > 0) {
      const aclClassCollectionIdentifiers = aclClassCollection.map(aclClassItem => getAclClassIdentifier(aclClassItem)!);
      const aclClassesToAdd = aclClasses.filter(aclClassItem => {
        const aclClassIdentifier = getAclClassIdentifier(aclClassItem);
        if (aclClassIdentifier == null || aclClassCollectionIdentifiers.includes(aclClassIdentifier)) {
          return false;
        }
        aclClassCollectionIdentifiers.push(aclClassIdentifier);
        return true;
      });
      return [...aclClassesToAdd, ...aclClassCollection];
    }
    return aclClassCollection;
  }
}
