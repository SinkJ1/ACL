jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAclMask, AclMask } from '../acl-mask.model';
import { AclMaskService } from '../service/acl-mask.service';

import { AclMaskRoutingResolveService } from './acl-mask-routing-resolve.service';

describe('Service Tests', () => {
  describe('AclMask routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AclMaskRoutingResolveService;
    let service: AclMaskService;
    let resultAclMask: IAclMask | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AclMaskRoutingResolveService);
      service = TestBed.inject(AclMaskService);
      resultAclMask = undefined;
    });

    describe('resolve', () => {
      it('should return IAclMask returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclMask = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclMask).toEqual({ id: 123 });
      });

      it('should return new IAclMask if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclMask = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAclMask).toEqual(new AclMask());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AclMask })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclMask = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclMask).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
