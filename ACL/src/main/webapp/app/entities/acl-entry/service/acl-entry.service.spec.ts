import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAclEntry, AclEntry } from '../acl-entry.model';

import { AclEntryService } from './acl-entry.service';

describe('Service Tests', () => {
  describe('AclEntry Service', () => {
    let service: AclEntryService;
    let httpMock: HttpTestingController;
    let elemDefault: IAclEntry;
    let expectedResult: IAclEntry | IAclEntry[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AclEntryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        aceOrder: 0,
        mask: 0,
        granting: false,
        auditSuccess: false,
        auditFailure: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AclEntry', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AclEntry()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AclEntry', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            aceOrder: 1,
            mask: 1,
            granting: true,
            auditSuccess: true,
            auditFailure: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AclEntry', () => {
        const patchObject = Object.assign(
          {
            auditSuccess: true,
          },
          new AclEntry()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AclEntry', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            aceOrder: 1,
            mask: 1,
            granting: true,
            auditSuccess: true,
            auditFailure: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AclEntry', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAclEntryToCollectionIfMissing', () => {
        it('should add a AclEntry to an empty array', () => {
          const aclEntry: IAclEntry = { id: 123 };
          expectedResult = service.addAclEntryToCollectionIfMissing([], aclEntry);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclEntry);
        });

        it('should not add a AclEntry to an array that contains it', () => {
          const aclEntry: IAclEntry = { id: 123 };
          const aclEntryCollection: IAclEntry[] = [
            {
              ...aclEntry,
            },
            { id: 456 },
          ];
          expectedResult = service.addAclEntryToCollectionIfMissing(aclEntryCollection, aclEntry);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AclEntry to an array that doesn't contain it", () => {
          const aclEntry: IAclEntry = { id: 123 };
          const aclEntryCollection: IAclEntry[] = [{ id: 456 }];
          expectedResult = service.addAclEntryToCollectionIfMissing(aclEntryCollection, aclEntry);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclEntry);
        });

        it('should add only unique AclEntry to an array', () => {
          const aclEntryArray: IAclEntry[] = [{ id: 123 }, { id: 456 }, { id: 18798 }];
          const aclEntryCollection: IAclEntry[] = [{ id: 123 }];
          expectedResult = service.addAclEntryToCollectionIfMissing(aclEntryCollection, ...aclEntryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const aclEntry: IAclEntry = { id: 123 };
          const aclEntry2: IAclEntry = { id: 456 };
          expectedResult = service.addAclEntryToCollectionIfMissing([], aclEntry, aclEntry2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclEntry);
          expect(expectedResult).toContain(aclEntry2);
        });

        it('should accept null and undefined values', () => {
          const aclEntry: IAclEntry = { id: 123 };
          expectedResult = service.addAclEntryToCollectionIfMissing([], null, aclEntry, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclEntry);
        });

        it('should return initial array if no AclEntry is added', () => {
          const aclEntryCollection: IAclEntry[] = [{ id: 123 }];
          expectedResult = service.addAclEntryToCollectionIfMissing(aclEntryCollection, undefined, null);
          expect(expectedResult).toEqual(aclEntryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
