import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPlanSubscription } from '../plan-subscription.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../plan-subscription.test-samples';

import { PlanSubscriptionService, RestPlanSubscription } from './plan-subscription.service';

const requireRestSample: RestPlanSubscription = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('PlanSubscription Service', () => {
  let service: PlanSubscriptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlanSubscription | IPlanSubscription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlanSubscriptionService);
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

    it('should create a PlanSubscription', () => {
      const planSubscription = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(planSubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlanSubscription', () => {
      const planSubscription = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(planSubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlanSubscription', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlanSubscription', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlanSubscription', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlanSubscriptionToCollectionIfMissing', () => {
      it('should add a PlanSubscription to an empty array', () => {
        const planSubscription: IPlanSubscription = sampleWithRequiredData;
        expectedResult = service.addPlanSubscriptionToCollectionIfMissing([], planSubscription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(planSubscription);
      });

      it('should not add a PlanSubscription to an array that contains it', () => {
        const planSubscription: IPlanSubscription = sampleWithRequiredData;
        const planSubscriptionCollection: IPlanSubscription[] = [
          {
            ...planSubscription,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlanSubscriptionToCollectionIfMissing(planSubscriptionCollection, planSubscription);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlanSubscription to an array that doesn't contain it", () => {
        const planSubscription: IPlanSubscription = sampleWithRequiredData;
        const planSubscriptionCollection: IPlanSubscription[] = [sampleWithPartialData];
        expectedResult = service.addPlanSubscriptionToCollectionIfMissing(planSubscriptionCollection, planSubscription);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planSubscription);
      });

      it('should add only unique PlanSubscription to an array', () => {
        const planSubscriptionArray: IPlanSubscription[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const planSubscriptionCollection: IPlanSubscription[] = [sampleWithRequiredData];
        expectedResult = service.addPlanSubscriptionToCollectionIfMissing(planSubscriptionCollection, ...planSubscriptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const planSubscription: IPlanSubscription = sampleWithRequiredData;
        const planSubscription2: IPlanSubscription = sampleWithPartialData;
        expectedResult = service.addPlanSubscriptionToCollectionIfMissing([], planSubscription, planSubscription2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planSubscription);
        expect(expectedResult).toContain(planSubscription2);
      });

      it('should accept null and undefined values', () => {
        const planSubscription: IPlanSubscription = sampleWithRequiredData;
        expectedResult = service.addPlanSubscriptionToCollectionIfMissing([], null, planSubscription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(planSubscription);
      });

      it('should return initial array if no PlanSubscription is added', () => {
        const planSubscriptionCollection: IPlanSubscription[] = [sampleWithRequiredData];
        expectedResult = service.addPlanSubscriptionToCollectionIfMissing(planSubscriptionCollection, undefined, null);
        expect(expectedResult).toEqual(planSubscriptionCollection);
      });
    });

    describe('comparePlanSubscription', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlanSubscription(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlanSubscription(entity1, entity2);
        const compareResult2 = service.comparePlanSubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlanSubscription(entity1, entity2);
        const compareResult2 = service.comparePlanSubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlanSubscription(entity1, entity2);
        const compareResult2 = service.comparePlanSubscription(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
