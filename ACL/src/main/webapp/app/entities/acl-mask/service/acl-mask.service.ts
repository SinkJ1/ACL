import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAclMask, getAclMaskIdentifier } from '../acl-mask.model';

export type EntityResponseType = HttpResponse<IAclMask>;
export type EntityArrayResponseType = HttpResponse<IAclMask[]>;

@Injectable({ providedIn: 'root' })
export class AclMaskService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acl-masks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aclMask: IAclMask): Observable<EntityResponseType> {
    return this.http.post<IAclMask>(this.resourceUrl, aclMask, { observe: 'response' });
  }

  update(aclMask: IAclMask): Observable<EntityResponseType> {
    return this.http.put<IAclMask>(`${this.resourceUrl}/${getAclMaskIdentifier(aclMask) as number}`, aclMask, { observe: 'response' });
  }

  partialUpdate(aclMask: IAclMask): Observable<EntityResponseType> {
    return this.http.patch<IAclMask>(`${this.resourceUrl}/${getAclMaskIdentifier(aclMask) as number}`, aclMask, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAclMask>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAclMask[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAclMaskToCollectionIfMissing(aclMaskCollection: IAclMask[], ...aclMasksToCheck: (IAclMask | null | undefined)[]): IAclMask[] {
    const aclMasks: IAclMask[] = aclMasksToCheck.filter(isPresent);
    if (aclMasks.length > 0) {
      const aclMaskCollectionIdentifiers = aclMaskCollection.map(aclMaskItem => getAclMaskIdentifier(aclMaskItem)!);
      const aclMasksToAdd = aclMasks.filter(aclMaskItem => {
        const aclMaskIdentifier = getAclMaskIdentifier(aclMaskItem);
        if (aclMaskIdentifier == null || aclMaskCollectionIdentifiers.includes(aclMaskIdentifier)) {
          return false;
        }
        aclMaskCollectionIdentifiers.push(aclMaskIdentifier);
        return true;
      });
      return [...aclMasksToAdd, ...aclMaskCollection];
    }
    return aclMaskCollection;
  }
}
