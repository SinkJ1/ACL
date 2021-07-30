import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAclEntry, getAclEntryIdentifier } from '../acl-entry.model';

export type EntityResponseType = HttpResponse<IAclEntry>;
export type EntityArrayResponseType = HttpResponse<IAclEntry[]>;

@Injectable({ providedIn: 'root' })
export class AclEntryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acl-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aclEntry: IAclEntry): Observable<EntityResponseType> {
    return this.http.post<IAclEntry>(this.resourceUrl, aclEntry, { observe: 'response' });
  }

  update(aclEntry: IAclEntry): Observable<EntityResponseType> {
    return this.http.put<IAclEntry>(`${this.resourceUrl}/${getAclEntryIdentifier(aclEntry) as number}`, aclEntry, { observe: 'response' });
  }

  partialUpdate(aclEntry: IAclEntry): Observable<EntityResponseType> {
    return this.http.patch<IAclEntry>(`${this.resourceUrl}/${getAclEntryIdentifier(aclEntry) as number}`, aclEntry, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAclEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAclEntry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAclEntryToCollectionIfMissing(aclEntryCollection: IAclEntry[], ...aclEntriesToCheck: (IAclEntry | null | undefined)[]): IAclEntry[] {
    const aclEntries: IAclEntry[] = aclEntriesToCheck.filter(isPresent);
    if (aclEntries.length > 0) {
      const aclEntryCollectionIdentifiers = aclEntryCollection.map(aclEntryItem => getAclEntryIdentifier(aclEntryItem)!);
      const aclEntriesToAdd = aclEntries.filter(aclEntryItem => {
        const aclEntryIdentifier = getAclEntryIdentifier(aclEntryItem);
        if (aclEntryIdentifier == null || aclEntryCollectionIdentifiers.includes(aclEntryIdentifier)) {
          return false;
        }
        aclEntryCollectionIdentifiers.push(aclEntryIdentifier);
        return true;
      });
      return [...aclEntriesToAdd, ...aclEntryCollection];
    }
    return aclEntryCollection;
  }
}
