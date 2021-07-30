jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAclSid, AclSid } from '../acl-sid.model';
import { AclSidService } from '../service/acl-sid.service';

import { AclSidRoutingResolveService } from './acl-sid-routing-resolve.service';

describe('Service Tests', () => {
  describe('AclSid routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AclSidRoutingResolveService;
    let service: AclSidService;
    let resultAclSid: IAclSid | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AclSidRoutingResolveService);
      service = TestBed.inject(AclSidService);
      resultAclSid = undefined;
    });

    describe('resolve', () => {
      it('should return IAclSid returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclSid = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclSid).toEqual({ id: 123 });
      });

      it('should return new IAclSid if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclSid = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAclSid).toEqual(new AclSid());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AclSid })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAclSid = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAclSid).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
