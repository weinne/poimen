import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHymn } from '../hymn.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../hymn.test-samples';

import { HymnService } from './hymn.service';

const requireRestSample: IHymn = {
  ...sampleWithRequiredData,
};

describe('Hymn Service', () => {
  let service: HymnService;
  let httpMock: HttpTestingController;
  let expectedResult: IHymn | IHymn[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HymnService);
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

    it('should create a Hymn', () => {
      const hymn = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hymn).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hymn', () => {
      const hymn = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hymn).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Hymn', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Hymn', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Hymn', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHymnToCollectionIfMissing', () => {
      it('should add a Hymn to an empty array', () => {
        const hymn: IHymn = sampleWithRequiredData;
        expectedResult = service.addHymnToCollectionIfMissing([], hymn);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hymn);
      });

      it('should not add a Hymn to an array that contains it', () => {
        const hymn: IHymn = sampleWithRequiredData;
        const hymnCollection: IHymn[] = [
          {
            ...hymn,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHymnToCollectionIfMissing(hymnCollection, hymn);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hymn to an array that doesn't contain it", () => {
        const hymn: IHymn = sampleWithRequiredData;
        const hymnCollection: IHymn[] = [sampleWithPartialData];
        expectedResult = service.addHymnToCollectionIfMissing(hymnCollection, hymn);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hymn);
      });

      it('should add only unique Hymn to an array', () => {
        const hymnArray: IHymn[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hymnCollection: IHymn[] = [sampleWithRequiredData];
        expectedResult = service.addHymnToCollectionIfMissing(hymnCollection, ...hymnArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hymn: IHymn = sampleWithRequiredData;
        const hymn2: IHymn = sampleWithPartialData;
        expectedResult = service.addHymnToCollectionIfMissing([], hymn, hymn2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hymn);
        expect(expectedResult).toContain(hymn2);
      });

      it('should accept null and undefined values', () => {
        const hymn: IHymn = sampleWithRequiredData;
        expectedResult = service.addHymnToCollectionIfMissing([], null, hymn, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hymn);
      });

      it('should return initial array if no Hymn is added', () => {
        const hymnCollection: IHymn[] = [sampleWithRequiredData];
        expectedResult = service.addHymnToCollectionIfMissing(hymnCollection, undefined, null);
        expect(expectedResult).toEqual(hymnCollection);
      });
    });

    describe('compareHymn', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHymn(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHymn(entity1, entity2);
        const compareResult2 = service.compareHymn(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHymn(entity1, entity2);
        const compareResult2 = service.compareHymn(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHymn(entity1, entity2);
        const compareResult2 = service.compareHymn(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
