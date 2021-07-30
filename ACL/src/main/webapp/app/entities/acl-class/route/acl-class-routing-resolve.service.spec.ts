jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAclClass, AclClass } from '../acl-class.model';
import { AclClassService } from '../service/acl-class.service';

import { AclClassRoutingResolveService } from './acl-class-routing-resolve.service';

describe('Service Tests', () => {
  describe('AclClass routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AclClassRoutingResolveService;
    let service: AclClassService;
    let resultAclClass: IAclClass | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AclClassRoutingResolveService);
      service = TestBed.inject(AclClassService);
      resultAclClass = undefined;
    });

    describe('resolve', () => {
      it('should return IAclClass returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclClass = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclClass).toEqual({ id: 123 });
      });

      it('should return new IAclClass if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclClass = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAclClass).toEqual(new AclClass());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AclClass })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclClass = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclClass).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
