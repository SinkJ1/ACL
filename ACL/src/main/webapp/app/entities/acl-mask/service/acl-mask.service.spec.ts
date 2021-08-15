import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAclMask, AclMask } from '../acl-mask.model';

import { AclMaskService } from './acl-mask.service';

describe('Service Tests', () => {
  describe('AclMask Service', () => {
    let service: AclMaskService;
    let httpMock: HttpTestingController;
    let elemDefault: IAclMask;
    let expectedResult: IAclMask | IAclMask[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AclMaskService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a AclMask', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AclMask()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AclMask', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AclMask', () => {
        const patchObject = Object.assign({}, new AclMask());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AclMask', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a AclMask', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAclMaskToCollectionIfMissing', () => {
        it('should add a AclMask to an empty array', () => {
          const aclMask: IAclMask = { id: 123 };
          expectedResult = service.addAclMaskToCollectionIfMissing([], aclMask);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclMask);
        });

        it('should not add a AclMask to an array that contains it', () => {
          const aclMask: IAclMask = { id: 123 };
          const aclMaskCollection: IAclMask[] = [
            {
              ...aclMask,
            },
            { id: 456 },
          ];
          expectedResult = service.addAclMaskToCollectionIfMissing(aclMaskCollection, aclMask);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AclMask to an array that doesn't contain it", () => {
          const aclMask: IAclMask = { id: 123 };
          const aclMaskCollection: IAclMask[] = [{ id: 456 }];
          expectedResult = service.addAclMaskToCollectionIfMissing(aclMaskCollection, aclMask);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclMask);
        });

        it('should add only unique AclMask to an array', () => {
          const aclMaskArray: IAclMask[] = [{ id: 123 }, { id: 456 }, { id: 57179 }];
          const aclMaskCollection: IAclMask[] = [{ id: 123 }];
          expectedResult = service.addAclMaskToCollectionIfMissing(aclMaskCollection, ...aclMaskArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const aclMask: IAclMask = { id: 123 };
          const aclMask2: IAclMask = { id: 456 };
          expectedResult = service.addAclMaskToCollectionIfMissing([], aclMask, aclMask2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclMask);
          expect(expectedResult).toContain(aclMask2);
        });

        it('should accept null and undefined values', () => {
          const aclMask: IAclMask = { id: 123 };
          expectedResult = service.addAclMaskToCollectionIfMissing([], null, aclMask, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclMask);
        });

        it('should return initial array if no AclMask is added', () => {
          const aclMaskCollection: IAclMask[] = [{ id: 123 }];
          expectedResult = service.addAclMaskToCollectionIfMissing(aclMaskCollection, undefined, null);
          expect(expectedResult).toEqual(aclMaskCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
