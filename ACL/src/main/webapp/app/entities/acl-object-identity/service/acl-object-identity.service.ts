import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAclObjectIdentity, getAclObjectIdentityIdentifier } from '../acl-object-identity.model';

export type EntityResponseType = HttpResponse<IAclObjectIdentity>;
export type EntityArrayResponseType = HttpResponse<IAclObjectIdentity[]>;

@Injectable({ providedIn: 'root' })
export class AclObjectIdentityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acl-object-identities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aclObjectIdentity: IAclObjectIdentity, headers: any): Observable<EntityResponseType> {
    return this.http.post<IAclObjectIdentity>(this.resourceUrl, aclObjectIdentity, { observe: 'response', headers });
  }

  update(aclObjectIdentity: IAclObjectIdentity, headers: any): Observable<EntityResponseType> {
    return this.http.put<IAclObjectIdentity>(
      `${this.resourceUrl}/${getAclObjectIdentityIdentifier(aclObjectIdentity) as number}`,
      aclObjectIdentity,
      { observe: 'response', headers }
    );
  }

  partialUpdate(aclObjectIdentity: IAclObjectIdentity, headers: any): Observable<EntityResponseType> {
    return this.http.patch<IAclObjectIdentity>(
      `${this.resourceUrl}/${getAclObjectIdentityIdentifier(aclObjectIdentity) as number}`,
      aclObjectIdentity,
      { observe: 'response', headers }
    );
  }

  find(id: number, headers: any): Observable<EntityResponseType> {
    return this.http.get<IAclObjectIdentity>(`${this.resourceUrl}/${id}`, { observe: 'response', headers });
  }

  query(req?: any, headers?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAclObjectIdentity[]>(this.resourceUrl, { params: options, observe: 'response', headers });
  }

  delete(id: number, headers: any): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response', headers });
  }

  addAclObjectIdentityToCollectionIfMissing(
    aclObjectIdentityCollection: IAclObjectIdentity[],
    ...aclObjectIdentitiesToCheck: (IAclObjectIdentity | null | undefined)[]
  ): IAclObjectIdentity[] {
    const aclObjectIdentities: IAclObjectIdentity[] = aclObjectIdentitiesToCheck.filter(isPresent);
    if (aclObjectIdentities.length > 0) {
      const aclObjectIdentityCollectionIdentifiers = aclObjectIdentityCollection.map(
        aclObjectIdentityItem => getAclObjectIdentityIdentifier(aclObjectIdentityItem)!
      );
      const aclObjectIdentitiesToAdd = aclObjectIdentities.filter(aclObjectIdentityItem => {
        const aclObjectIdentityIdentifier = getAclObjectIdentityIdentifier(aclObjectIdentityItem);
        if (aclObjectIdentityIdentifier == null || aclObjectIdentityCollectionIdentifiers.includes(aclObjectIdentityIdentifier)) {
          return false;
        }
        aclObjectIdentityCollectionIdentifiers.push(aclObjectIdentityIdentifier);
        return true;
      });
      return [...aclObjectIdentitiesToAdd, ...aclObjectIdentityCollection];
    }
    return aclObjectIdentityCollection;
  }
}
