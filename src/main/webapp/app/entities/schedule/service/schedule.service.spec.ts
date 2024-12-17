import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISchedule } from '../schedule.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../schedule.test-samples';

import { RestSchedule, ScheduleService } from './schedule.service';

const requireRestSample: RestSchedule = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('Schedule Service', () => {
  let service: ScheduleService;
  let httpMock: HttpTestingController;
  let expectedResult: ISchedule | ISchedule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScheduleService);
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

    it('should create a Schedule', () => {
      const schedule = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(schedule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Schedule', () => {
      const schedule = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(schedule).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Schedule', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Schedule', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Schedule', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addScheduleToCollectionIfMissing', () => {
      it('should add a Schedule to an empty array', () => {
        const schedule: ISchedule = sampleWithRequiredData;
        expectedResult = service.addScheduleToCollectionIfMissing([], schedule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(schedule);
      });

      it('should not add a Schedule to an array that contains it', () => {
        const schedule: ISchedule = sampleWithRequiredData;
        const scheduleCollection: ISchedule[] = [
          {
            ...schedule,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScheduleToCollectionIfMissing(scheduleCollection, schedule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Schedule to an array that doesn't contain it", () => {
        const schedule: ISchedule = sampleWithRequiredData;
        const scheduleCollection: ISchedule[] = [sampleWithPartialData];
        expectedResult = service.addScheduleToCollectionIfMissing(scheduleCollection, schedule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(schedule);
      });

      it('should add only unique Schedule to an array', () => {
        const scheduleArray: ISchedule[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scheduleCollection: ISchedule[] = [sampleWithRequiredData];
        expectedResult = service.addScheduleToCollectionIfMissing(scheduleCollection, ...scheduleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const schedule: ISchedule = sampleWithRequiredData;
        const schedule2: ISchedule = sampleWithPartialData;
        expectedResult = service.addScheduleToCollectionIfMissing([], schedule, schedule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(schedule);
        expect(expectedResult).toContain(schedule2);
      });

      it('should accept null and undefined values', () => {
        const schedule: ISchedule = sampleWithRequiredData;
        expectedResult = service.addScheduleToCollectionIfMissing([], null, schedule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(schedule);
      });

      it('should return initial array if no Schedule is added', () => {
        const scheduleCollection: ISchedule[] = [sampleWithRequiredData];
        expectedResult = service.addScheduleToCollectionIfMissing(scheduleCollection, undefined, null);
        expect(expectedResult).toEqual(scheduleCollection);
      });
    });

    describe('compareSchedule', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSchedule(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSchedule(entity1, entity2);
        const compareResult2 = service.compareSchedule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSchedule(entity1, entity2);
        const compareResult2 = service.compareSchedule(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSchedule(entity1, entity2);
        const compareResult2 = service.compareSchedule(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
