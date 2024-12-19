import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IChurch } from '../church.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../church.test-samples';

import { ChurchService, RestChurch } from './church.service';

const requireRestSample: RestChurch = {
  ...sampleWithRequiredData,
  dateFoundation: sampleWithRequiredData.dateFoundation?.format(DATE_FORMAT),
};

describe('Church Service', () => {
  let service: ChurchService;
  let httpMock: HttpTestingController;
  let expectedResult: IChurch | IChurch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ChurchService);
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

    it('should create a Church', () => {
      const church = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(church).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Church', () => {
      const church = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(church).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Church', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Church', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Church', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Church', () => {
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

    describe('addChurchToCollectionIfMissing', () => {
      it('should add a Church to an empty array', () => {
        const church: IChurch = sampleWithRequiredData;
        expectedResult = service.addChurchToCollectionIfMissing([], church);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(church);
      });

      it('should not add a Church to an array that contains it', () => {
        const church: IChurch = sampleWithRequiredData;
        const churchCollection: IChurch[] = [
          {
            ...church,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChurchToCollectionIfMissing(churchCollection, church);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Church to an array that doesn't contain it", () => {
        const church: IChurch = sampleWithRequiredData;
        const churchCollection: IChurch[] = [sampleWithPartialData];
        expectedResult = service.addChurchToCollectionIfMissing(churchCollection, church);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(church);
      });

      it('should add only unique Church to an array', () => {
        const churchArray: IChurch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const churchCollection: IChurch[] = [sampleWithRequiredData];
        expectedResult = service.addChurchToCollectionIfMissing(churchCollection, ...churchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const church: IChurch = sampleWithRequiredData;
        const church2: IChurch = sampleWithPartialData;
        expectedResult = service.addChurchToCollectionIfMissing([], church, church2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(church);
        expect(expectedResult).toContain(church2);
      });

      it('should accept null and undefined values', () => {
        const church: IChurch = sampleWithRequiredData;
        expectedResult = service.addChurchToCollectionIfMissing([], null, church, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(church);
      });

      it('should return initial array if no Church is added', () => {
        const churchCollection: IChurch[] = [sampleWithRequiredData];
        expectedResult = service.addChurchToCollectionIfMissing(churchCollection, undefined, null);
        expect(expectedResult).toEqual(churchCollection);
      });
    });

    describe('compareChurch', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareChurch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareChurch(entity1, entity2);
        const compareResult2 = service.compareChurch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareChurch(entity1, entity2);
        const compareResult2 = service.compareChurch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareChurch(entity1, entity2);
        const compareResult2 = service.compareChurch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
