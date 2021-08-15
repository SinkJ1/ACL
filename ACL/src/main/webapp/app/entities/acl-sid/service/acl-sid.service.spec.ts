import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAclSid, AclSid } from '../acl-sid.model';

import { AclSidService } from './acl-sid.service';

describe('Service Tests', () => {
  describe('AclSid Service', () => {
    let service: AclSidService;
    let httpMock: HttpTestingController;
    let elemDefault: IAclSid;
    let expectedResult: IAclSid | IAclSid[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AclSidService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        sid: 'AAAAAAA',
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

      it('should create a AclSid', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AclSid()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AclSid', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sid: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AclSid', () => {
        const patchObject = Object.assign(
          {
            sid: 'BBBBBB',
          },
          new AclSid()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AclSid', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sid: 'BBBBBB',
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

      it('should delete a AclSid', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAclSidToCollectionIfMissing', () => {
        it('should add a AclSid to an empty array', () => {
          const aclSid: IAclSid = { id: 123 };
          expectedResult = service.addAclSidToCollectionIfMissing([], aclSid);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclSid);
        });

        it('should not add a AclSid to an array that contains it', () => {
          const aclSid: IAclSid = { id: 123 };
          const aclSidCollection: IAclSid[] = [
            {
              ...aclSid,
            },
            { id: 456 },
          ];
          expectedResult = service.addAclSidToCollectionIfMissing(aclSidCollection, aclSid);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AclSid to an array that doesn't contain it", () => {
          const aclSid: IAclSid = { id: 123 };
          const aclSidCollection: IAclSid[] = [{ id: 456 }];
          expectedResult = service.addAclSidToCollectionIfMissing(aclSidCollection, aclSid);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclSid);
        });

        it('should add only unique AclSid to an array', () => {
          const aclSidArray: IAclSid[] = [{ id: 123 }, { id: 456 }, { id: 48959 }];
          const aclSidCollection: IAclSid[] = [{ id: 123 }];
          expectedResult = service.addAclSidToCollectionIfMissing(aclSidCollection, ...aclSidArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const aclSid: IAclSid = { id: 123 };
          const aclSid2: IAclSid = { id: 456 };
          expectedResult = service.addAclSidToCollectionIfMissing([], aclSid, aclSid2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclSid);
          expect(expectedResult).toContain(aclSid2);
        });

        it('should accept null and undefined values', () => {
          const aclSid: IAclSid = { id: 123 };
          expectedResult = service.addAclSidToCollectionIfMissing([], null, aclSid, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclSid);
        });

        it('should return initial array if no AclSid is added', () => {
          const aclSidCollection: IAclSid[] = [{ id: 123 }];
          expectedResult = service.addAclSidToCollectionIfMissing(aclSidCollection, undefined, null);
          expect(expectedResult).toEqual(aclSidCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
