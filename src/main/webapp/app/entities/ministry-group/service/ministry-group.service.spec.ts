import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMinistryGroup } from '../ministry-group.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ministry-group.test-samples';

import { MinistryGroupService, RestMinistryGroup } from './ministry-group.service';

const requireRestSample: RestMinistryGroup = {
  ...sampleWithRequiredData,
  establishedDate: sampleWithRequiredData.establishedDate?.format(DATE_FORMAT),
};

describe('MinistryGroup Service', () => {
  let service: MinistryGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IMinistryGroup | IMinistryGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MinistryGroupService);
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

    it('should create a MinistryGroup', () => {
      const ministryGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ministryGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MinistryGroup', () => {
      const ministryGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ministryGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MinistryGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MinistryGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MinistryGroup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MinistryGroup', () => {
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

    describe('addMinistryGroupToCollectionIfMissing', () => {
      it('should add a MinistryGroup to an empty array', () => {
        const ministryGroup: IMinistryGroup = sampleWithRequiredData;
        expectedResult = service.addMinistryGroupToCollectionIfMissing([], ministryGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministryGroup);
      });

      it('should not add a MinistryGroup to an array that contains it', () => {
        const ministryGroup: IMinistryGroup = sampleWithRequiredData;
        const ministryGroupCollection: IMinistryGroup[] = [
          {
            ...ministryGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMinistryGroupToCollectionIfMissing(ministryGroupCollection, ministryGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MinistryGroup to an array that doesn't contain it", () => {
        const ministryGroup: IMinistryGroup = sampleWithRequiredData;
        const ministryGroupCollection: IMinistryGroup[] = [sampleWithPartialData];
        expectedResult = service.addMinistryGroupToCollectionIfMissing(ministryGroupCollection, ministryGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministryGroup);
      });

      it('should add only unique MinistryGroup to an array', () => {
        const ministryGroupArray: IMinistryGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ministryGroupCollection: IMinistryGroup[] = [sampleWithRequiredData];
        expectedResult = service.addMinistryGroupToCollectionIfMissing(ministryGroupCollection, ...ministryGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ministryGroup: IMinistryGroup = sampleWithRequiredData;
        const ministryGroup2: IMinistryGroup = sampleWithPartialData;
        expectedResult = service.addMinistryGroupToCollectionIfMissing([], ministryGroup, ministryGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministryGroup);
        expect(expectedResult).toContain(ministryGroup2);
      });

      it('should accept null and undefined values', () => {
        const ministryGroup: IMinistryGroup = sampleWithRequiredData;
        expectedResult = service.addMinistryGroupToCollectionIfMissing([], null, ministryGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministryGroup);
      });

      it('should return initial array if no MinistryGroup is added', () => {
        const ministryGroupCollection: IMinistryGroup[] = [sampleWithRequiredData];
        expectedResult = service.addMinistryGroupToCollectionIfMissing(ministryGroupCollection, undefined, null);
        expect(expectedResult).toEqual(ministryGroupCollection);
      });
    });

    describe('compareMinistryGroup', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMinistryGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMinistryGroup(entity1, entity2);
        const compareResult2 = service.compareMinistryGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMinistryGroup(entity1, entity2);
        const compareResult2 = service.compareMinistryGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMinistryGroup(entity1, entity2);
        const compareResult2 = service.compareMinistryGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
