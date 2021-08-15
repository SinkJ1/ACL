import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAclClass, AclClass } from '../acl-class.model';

import { AclClassService } from './acl-class.service';

describe('Service Tests', () => {
  describe('AclClass Service', () => {
    let service: AclClassService;
    let httpMock: HttpTestingController;
    let elemDefault: IAclClass;
    let expectedResult: IAclClass | IAclClass[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AclClassService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        className: 'AAAAAAA',
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

      it('should create a AclClass', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AclClass()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AclClass', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            className: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AclClass', () => {
        const patchObject = Object.assign({}, new AclClass());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AclClass', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            className: 'BBBBBB',
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

      it('should delete a AclClass', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAclClassToCollectionIfMissing', () => {
        it('should add a AclClass to an empty array', () => {
          const aclClass: IAclClass = { id: 123 };
          expectedResult = service.addAclClassToCollectionIfMissing([], aclClass);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclClass);
        });

        it('should not add a AclClass to an array that contains it', () => {
          const aclClass: IAclClass = { id: 123 };
          const aclClassCollection: IAclClass[] = [
            {
              ...aclClass,
            },
            { id: 456 },
          ];
          expectedResult = service.addAclClassToCollectionIfMissing(aclClassCollection, aclClass);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AclClass to an array that doesn't contain it", () => {
          const aclClass: IAclClass = { id: 123 };
          const aclClassCollection: IAclClass[] = [{ id: 456 }];
          expectedResult = service.addAclClassToCollectionIfMissing(aclClassCollection, aclClass);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclClass);
        });

        it('should add only unique AclClass to an array', () => {
          const aclClassArray: IAclClass[] = [{ id: 123 }, { id: 456 }, { id: 90719 }];
          const aclClassCollection: IAclClass[] = [{ id: 123 }];
          expectedResult = service.addAclClassToCollectionIfMissing(aclClassCollection, ...aclClassArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const aclClass: IAclClass = { id: 123 };
          const aclClass2: IAclClass = { id: 456 };
          expectedResult = service.addAclClassToCollectionIfMissing([], aclClass, aclClass2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aclClass);
          expect(expectedResult).toContain(aclClass2);
        });

        it('should accept null and undefined values', () => {
          const aclClass: IAclClass = { id: 123 };
          expectedResult = service.addAclClassToCollectionIfMissing([], null, aclClass, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aclClass);
        });

        it('should return initial array if no AclClass is added', () => {
          const aclClassCollection: IAclClass[] = [{ id: 123 }];
          expectedResult = service.addAclClassToCollectionIfMissing(aclClassCollection, undefined, null);
          expect(expectedResult).toEqual(aclClassCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
