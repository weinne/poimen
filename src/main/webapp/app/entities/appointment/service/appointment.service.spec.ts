import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAppointment } from '../appointment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../appointment.test-samples';

import { AppointmentService, RestAppointment } from './appointment.service';

const requireRestSample: RestAppointment = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('Appointment Service', () => {
  let service: AppointmentService;
  let httpMock: HttpTestingController;
  let expectedResult: IAppointment | IAppointment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AppointmentService);
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

    it('should create a Appointment', () => {
      const appointment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(appointment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Appointment', () => {
      const appointment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(appointment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Appointment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Appointment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Appointment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Appointment', () => {
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

    describe('addAppointmentToCollectionIfMissing', () => {
      it('should add a Appointment to an empty array', () => {
        const appointment: IAppointment = sampleWithRequiredData;
        expectedResult = service.addAppointmentToCollectionIfMissing([], appointment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appointment);
      });

      it('should not add a Appointment to an array that contains it', () => {
        const appointment: IAppointment = sampleWithRequiredData;
        const appointmentCollection: IAppointment[] = [
          {
            ...appointment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppointmentToCollectionIfMissing(appointmentCollection, appointment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Appointment to an array that doesn't contain it", () => {
        const appointment: IAppointment = sampleWithRequiredData;
        const appointmentCollection: IAppointment[] = [sampleWithPartialData];
        expectedResult = service.addAppointmentToCollectionIfMissing(appointmentCollection, appointment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appointment);
      });

      it('should add only unique Appointment to an array', () => {
        const appointmentArray: IAppointment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appointmentCollection: IAppointment[] = [sampleWithRequiredData];
        expectedResult = service.addAppointmentToCollectionIfMissing(appointmentCollection, ...appointmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appointment: IAppointment = sampleWithRequiredData;
        const appointment2: IAppointment = sampleWithPartialData;
        expectedResult = service.addAppointmentToCollectionIfMissing([], appointment, appointment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appointment);
        expect(expectedResult).toContain(appointment2);
      });

      it('should accept null and undefined values', () => {
        const appointment: IAppointment = sampleWithRequiredData;
        expectedResult = service.addAppointmentToCollectionIfMissing([], null, appointment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appointment);
      });

      it('should return initial array if no Appointment is added', () => {
        const appointmentCollection: IAppointment[] = [sampleWithRequiredData];
        expectedResult = service.addAppointmentToCollectionIfMissing(appointmentCollection, undefined, null);
        expect(expectedResult).toEqual(appointmentCollection);
      });
    });

    describe('compareAppointment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAppointment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAppointment(entity1, entity2);
        const compareResult2 = service.compareAppointment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAppointment(entity1, entity2);
        const compareResult2 = service.compareAppointment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAppointment(entity1, entity2);
        const compareResult2 = service.compareAppointment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
