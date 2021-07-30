import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAclSid, getAclSidIdentifier } from '../acl-sid.model';

export type EntityResponseType = HttpResponse<IAclSid>;
export type EntityArrayResponseType = HttpResponse<IAclSid[]>;

@Injectable({ providedIn: 'root' })
export class AclSidService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acl-sids');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aclSid: IAclSid): Observable<EntityResponseType> {
    return this.http.post<IAclSid>(this.resourceUrl, aclSid, { observe: 'response' });
  }

  update(aclSid: IAclSid): Observable<EntityResponseType> {
    return this.http.put<IAclSid>(`${this.resourceUrl}/${getAclSidIdentifier(aclSid) as number}`, aclSid, { observe: 'response' });
  }

  partialUpdate(aclSid: IAclSid): Observable<EntityResponseType> {
    return this.http.patch<IAclSid>(`${this.resourceUrl}/${getAclSidIdentifier(aclSid) as number}`, aclSid, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAclSid>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAclSid[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAclSidToCollectionIfMissing(aclSidCollection: IAclSid[], ...aclSidsToCheck: (IAclSid | null | undefined)[]): IAclSid[] {
    const aclSids: IAclSid[] = aclSidsToCheck.filter(isPresent);
    if (aclSids.length > 0) {
      const aclSidCollectionIdentifiers = aclSidCollection.map(aclSidItem => getAclSidIdentifier(aclSidItem)!);
      const aclSidsToAdd = aclSids.filter(aclSidItem => {
        const aclSidIdentifier = getAclSidIdentifier(aclSidItem);
        if (aclSidIdentifier == null || aclSidCollectionIdentifiers.includes(aclSidIdentifier)) {
          return false;
        }
        aclSidCollectionIdentifiers.push(aclSidIdentifier);
        return true;
      });
      return [...aclSidsToAdd, ...aclSidCollection];
    }
    return aclSidCollection;
  }
}
