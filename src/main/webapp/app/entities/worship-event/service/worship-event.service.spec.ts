import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWorshipEvent } from '../worship-event.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../worship-event.test-samples';

import { RestWorshipEvent, WorshipEventService } from './worship-event.service';

const requireRestSample: RestWorshipEvent = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('WorshipEvent Service', () => {
  let service: WorshipEventService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorshipEvent | IWorshipEvent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WorshipEventService);
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

    it('should create a WorshipEvent', () => {
      const worshipEvent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(worshipEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorshipEvent', () => {
      const worshipEvent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(worshipEvent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorshipEvent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorshipEvent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorshipEvent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a WorshipEvent', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addWorshipEventToCollectionIfMissing', () => {
      it('should add a WorshipEvent to an empty array', () => {
        const worshipEvent: IWorshipEvent = sampleWithRequiredData;
        expectedResult = service.addWorshipEventToCollectionIfMissing([], worshipEvent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(worshipEvent);
      });

      it('should not add a WorshipEvent to an array that contains it', () => {
        const worshipEvent: IWorshipEvent = sampleWithRequiredData;
        const worshipEventCollection: IWorshipEvent[] = [
          {
            ...worshipEvent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorshipEventToCollectionIfMissing(worshipEventCollection, worshipEvent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorshipEvent to an array that doesn't contain it", () => {
        const worshipEvent: IWorshipEvent = sampleWithRequiredData;
        const worshipEventCollection: IWorshipEvent[] = [sampleWithPartialData];
        expectedResult = service.addWorshipEventToCollectionIfMissing(worshipEventCollection, worshipEvent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(worshipEvent);
      });

      it('should add only unique WorshipEvent to an array', () => {
        const worshipEventArray: IWorshipEvent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const worshipEventCollection: IWorshipEvent[] = [sampleWithRequiredData];
        expectedResult = service.addWorshipEventToCollectionIfMissing(worshipEventCollection, ...worshipEventArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const worshipEvent: IWorshipEvent = sampleWithRequiredData;
        const worshipEvent2: IWorshipEvent = sampleWithPartialData;
        expectedResult = service.addWorshipEventToCollectionIfMissing([], worshipEvent, worshipEvent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(worshipEvent);
        expect(expectedResult).toContain(worshipEvent2);
      });

      it('should accept null and undefined values', () => {
        const worshipEvent: IWorshipEvent = sampleWithRequiredData;
        expectedResult = service.addWorshipEventToCollectionIfMissing([], null, worshipEvent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(worshipEvent);
      });

      it('should return initial array if no WorshipEvent is added', () => {
        const worshipEventCollection: IWorshipEvent[] = [sampleWithRequiredData];
        expectedResult = service.addWorshipEventToCollectionIfMissing(worshipEventCollection, undefined, null);
        expect(expectedResult).toEqual(worshipEventCollection);
      });
    });

    describe('compareWorshipEvent', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorshipEvent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWorshipEvent(entity1, entity2);
        const compareResult2 = service.compareWorshipEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWorshipEvent(entity1, entity2);
        const compareResult2 = service.compareWorshipEvent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWorshipEvent(entity1, entity2);
        const compareResult2 = service.compareWorshipEvent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
