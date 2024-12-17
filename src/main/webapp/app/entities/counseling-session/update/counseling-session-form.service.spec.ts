import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../counseling-session.test-samples';

import { CounselingSessionFormService } from './counseling-session-form.service';

describe('CounselingSession Form Service', () => {
  let service: CounselingSessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CounselingSessionFormService);
  });

  describe('Service methods', () => {
    describe('createCounselingSessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCounselingSessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            notes: expect.any(Object),
            church: expect.any(Object),
            member: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing ICounselingSession should create a new form with FormGroup', () => {
        const formGroup = service.createCounselingSessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            notes: expect.any(Object),
            church: expect.any(Object),
            member: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getCounselingSession', () => {
      it('should return NewCounselingSession for default CounselingSession initial value', () => {
        const formGroup = service.createCounselingSessionFormGroup(sampleWithNewData);

        const counselingSession = service.getCounselingSession(formGroup) as any;

        expect(counselingSession).toMatchObject(sampleWithNewData);
      });

      it('should return NewCounselingSession for empty CounselingSession initial value', () => {
        const formGroup = service.createCounselingSessionFormGroup();

        const counselingSession = service.getCounselingSession(formGroup) as any;

        expect(counselingSession).toMatchObject({});
      });

      it('should return ICounselingSession', () => {
        const formGroup = service.createCounselingSessionFormGroup(sampleWithRequiredData);

        const counselingSession = service.getCounselingSession(formGroup) as any;

        expect(counselingSession).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICounselingSession should not enable id FormControl', () => {
        const formGroup = service.createCounselingSessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCounselingSession should disable id FormControl', () => {
        const formGroup = service.createCounselingSessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
