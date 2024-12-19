import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICounselingSession } from '../counseling-session.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../counseling-session.test-samples';

import { CounselingSessionService, RestCounselingSession } from './counseling-session.service';

const requireRestSample: RestCounselingSession = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('CounselingSession Service', () => {
  let service: CounselingSessionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICounselingSession | ICounselingSession[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CounselingSessionService);
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

    it('should create a CounselingSession', () => {
      const counselingSession = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(counselingSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CounselingSession', () => {
      const counselingSession = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(counselingSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CounselingSession', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CounselingSession', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CounselingSession', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a CounselingSession', () => {
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

    describe('addCounselingSessionToCollectionIfMissing', () => {
      it('should add a CounselingSession to an empty array', () => {
        const counselingSession: ICounselingSession = sampleWithRequiredData;
        expectedResult = service.addCounselingSessionToCollectionIfMissing([], counselingSession);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counselingSession);
      });

      it('should not add a CounselingSession to an array that contains it', () => {
        const counselingSession: ICounselingSession = sampleWithRequiredData;
        const counselingSessionCollection: ICounselingSession[] = [
          {
            ...counselingSession,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCounselingSessionToCollectionIfMissing(counselingSessionCollection, counselingSession);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CounselingSession to an array that doesn't contain it", () => {
        const counselingSession: ICounselingSession = sampleWithRequiredData;
        const counselingSessionCollection: ICounselingSession[] = [sampleWithPartialData];
        expectedResult = service.addCounselingSessionToCollectionIfMissing(counselingSessionCollection, counselingSession);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counselingSession);
      });

      it('should add only unique CounselingSession to an array', () => {
        const counselingSessionArray: ICounselingSession[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const counselingSessionCollection: ICounselingSession[] = [sampleWithRequiredData];
        expectedResult = service.addCounselingSessionToCollectionIfMissing(counselingSessionCollection, ...counselingSessionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const counselingSession: ICounselingSession = sampleWithRequiredData;
        const counselingSession2: ICounselingSession = sampleWithPartialData;
        expectedResult = service.addCounselingSessionToCollectionIfMissing([], counselingSession, counselingSession2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(counselingSession);
        expect(expectedResult).toContain(counselingSession2);
      });

      it('should accept null and undefined values', () => {
        const counselingSession: ICounselingSession = sampleWithRequiredData;
        expectedResult = service.addCounselingSessionToCollectionIfMissing([], null, counselingSession, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(counselingSession);
      });

      it('should return initial array if no CounselingSession is added', () => {
        const counselingSessionCollection: ICounselingSession[] = [sampleWithRequiredData];
        expectedResult = service.addCounselingSessionToCollectionIfMissing(counselingSessionCollection, undefined, null);
        expect(expectedResult).toEqual(counselingSessionCollection);
      });
    });

    describe('compareCounselingSession', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCounselingSession(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCounselingSession(entity1, entity2);
        const compareResult2 = service.compareCounselingSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCounselingSession(entity1, entity2);
        const compareResult2 = service.compareCounselingSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCounselingSession(entity1, entity2);
        const compareResult2 = service.compareCounselingSession(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
