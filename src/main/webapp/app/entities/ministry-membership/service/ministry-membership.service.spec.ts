import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMinistryMembership } from '../ministry-membership.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ministry-membership.test-samples';

import { MinistryMembershipService, RestMinistryMembership } from './ministry-membership.service';

const requireRestSample: RestMinistryMembership = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('MinistryMembership Service', () => {
  let service: MinistryMembershipService;
  let httpMock: HttpTestingController;
  let expectedResult: IMinistryMembership | IMinistryMembership[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MinistryMembershipService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MinistryMembership', () => {
      const ministryMembership = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ministryMembership).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MinistryMembership', () => {
      const ministryMembership = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ministryMembership).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MinistryMembership', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MinistryMembership', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MinistryMembership', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMinistryMembershipToCollectionIfMissing', () => {
      it('should add a MinistryMembership to an empty array', () => {
        const ministryMembership: IMinistryMembership = sampleWithRequiredData;
        expectedResult = service.addMinistryMembershipToCollectionIfMissing([], ministryMembership);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministryMembership);
      });

      it('should not add a MinistryMembership to an array that contains it', () => {
        const ministryMembership: IMinistryMembership = sampleWithRequiredData;
        const ministryMembershipCollection: IMinistryMembership[] = [
          {
            ...ministryMembership,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMinistryMembershipToCollectionIfMissing(ministryMembershipCollection, ministryMembership);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MinistryMembership to an array that doesn't contain it", () => {
        const ministryMembership: IMinistryMembership = sampleWithRequiredData;
        const ministryMembershipCollection: IMinistryMembership[] = [sampleWithPartialData];
        expectedResult = service.addMinistryMembershipToCollectionIfMissing(ministryMembershipCollection, ministryMembership);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministryMembership);
      });

      it('should add only unique MinistryMembership to an array', () => {
        const ministryMembershipArray: IMinistryMembership[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ministryMembershipCollection: IMinistryMembership[] = [sampleWithRequiredData];
        expectedResult = service.addMinistryMembershipToCollectionIfMissing(ministryMembershipCollection, ...ministryMembershipArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ministryMembership: IMinistryMembership = sampleWithRequiredData;
        const ministryMembership2: IMinistryMembership = sampleWithPartialData;
        expectedResult = service.addMinistryMembershipToCollectionIfMissing([], ministryMembership, ministryMembership2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministryMembership);
        expect(expectedResult).toContain(ministryMembership2);
      });

      it('should accept null and undefined values', () => {
        const ministryMembership: IMinistryMembership = sampleWithRequiredData;
        expectedResult = service.addMinistryMembershipToCollectionIfMissing([], null, ministryMembership, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministryMembership);
      });

      it('should return initial array if no MinistryMembership is added', () => {
        const ministryMembershipCollection: IMinistryMembership[] = [sampleWithRequiredData];
        expectedResult = service.addMinistryMembershipToCollectionIfMissing(ministryMembershipCollection, undefined, null);
        expect(expectedResult).toEqual(ministryMembershipCollection);
      });
    });

    describe('compareMinistryMembership', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMinistryMembership(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMinistryMembership(entity1, entity2);
        const compareResult2 = service.compareMinistryMembership(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMinistryMembership(entity1, entity2);
        const compareResult2 = service.compareMinistryMembership(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMinistryMembership(entity1, entity2);
        const compareResult2 = service.compareMinistryMembership(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
