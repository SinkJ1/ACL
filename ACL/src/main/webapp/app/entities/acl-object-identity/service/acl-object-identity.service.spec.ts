import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAclObjectIdentity, AclObjectIdentity } from '../acl-object-identity.model';

import { AclObjectIdentityService } from './acl-object-identity.service';

describe('Service Tests', () => {
  describe('AclObjectIdentity Service', () => {
    let service: AclObjectIdentityService;
    let httpMock: HttpTestingController;
    let elemDefault: IAclObjectIdentity;
    let expectedResult: IAclObjectIdentity | IAclObjectIdentity[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AclObjectIdentityService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        objectIdIdentity: 'AAAAAAA',
        parentObject: 0,
        ownerSid: 0,
        entriesInheriting: false,
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

      it('should create a AclObjectIdentity', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AclObjectIdentity()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AclObjectIdentity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            objectIdIdentity: 'BBBBBB',
            parentObject: 1,
            ownerSid: 1,
            entriesInheriting: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AclObjectIdentity', () => {
        const patchObject = Object.assign(
          {
            objectIdIdentity: 'BBBBBB',
            ownerSid: 1,
            entriesInheriting: true,
          },
          new AclObjectIdentity()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AclObjectIdentity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            objectIdIdentity: 'BBBBBB',
            parentObject: 1,
            ownerSid: 1,
            entriesInheriting: true,
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

      it('should delete a AclObjectIdentity', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAclObjectIdentityToCollectionIfMissing', () => {
        it('should add a AclObjectIdentity to an empty array', () => {
          const aclObjectIdentity: IAclObjectIdentity = { id: 123 };
          expectedResult = service.addAclObjectIdentityToCollectionIfMissing([], aclObjectIdentity);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclObjectIdentity);
        });

        it('should not add a AclObjectIdentity to an array that contains it', () => {
          const aclObjectIdentity: IAclObjectIdentity = { id: 123 };
          const aclObjectIdentityCollection: IAclObjectIdentity[] = [
            {
              ...aclObjectIdentity,
            },
            { id: 456 },
          ];
          expectedResult = service.addAclObjectIdentityToCollectionIfMissing(aclObjectIdentityCollection, aclObjectIdentity);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AclObjectIdentity to an array that doesn't contain it", () => {
          const aclObjectIdentity: IAclObjectIdentity = { id: 123 };
          const aclObjectIdentityCollection: IAclObjectIdentity[] = [{ id: 456 }];
          expectedResult = service.addAclObjectIdentityToCollectionIfMissing(aclObjectIdentityCollection, aclObjectIdentity);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclObjectIdentity);
        });

        it('should add only unique AclObjectIdentity to an array', () => {
          const aclObjectIdentityArray: IAclObjectIdentity[] = [{ id: 123 }, { id: 456 }, { id: 28917 }];
          const aclObjectIdentityCollection: IAclObjectIdentity[] = [{ id: 123 }];
          expectedResult = service.addAclObjectIdentityToCollectionIfMissing(aclObjectIdentityCollection, ...aclObjectIdentityArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const aclObjectIdentity: IAclObjectIdentity = { id: 123 };
          const aclObjectIdentity2: IAclObjectIdentity = { id: 456 };
          expectedResult = service.addAclObjectIdentityToCollectionIfMissing([], aclObjectIdentity, aclObjectIdentity2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclObjectIdentity);
          expect(expectedResult).toContain(aclObjectIdentity2);
        });

        it('should accept null and undefined values', () => {
          const aclObjectIdentity: IAclObjectIdentity = { id: 123 };
          expectedResult = service.addAclObjectIdentityToCollectionIfMissing([], null, aclObjectIdentity, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclObjectIdentity);
        });

        it('should return initial array if no AclObjectIdentity is added', () => {
          const aclObjectIdentityCollection: IAclObjectIdentity[] = [{ id: 123 }];
          expectedResult = service.addAclObjectIdentityToCollectionIfMissing(aclObjectIdentityCollection, undefined, null);
          expect(expectedResult).toEqual(aclObjectIdentityCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
