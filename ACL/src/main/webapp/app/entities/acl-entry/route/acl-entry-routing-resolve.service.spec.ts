jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAclEntry, AclEntry } from '../acl-entry.model';
import { AclEntryService } from '../service/acl-entry.service';

import { AclEntryRoutingResolveService } from './acl-entry-routing-resolve.service';

describe('Service Tests', () => {
  describe('AclEntry routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AclEntryRoutingResolveService;
    let service: AclEntryService;
    let resultAclEntry: IAclEntry | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AclEntryRoutingResolveService);
      service = TestBed.inject(AclEntryService);
      resultAclEntry = undefined;
    });

    describe('resolve', () => {
      it('should return IAclEntry returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclEntry = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclEntry).toEqual({ id: 123 });
      });

      it('should return new IAclEntry if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclEntry = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAclEntry).toEqual(new AclEntry());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AclEntry })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclEntry = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclEntry).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
