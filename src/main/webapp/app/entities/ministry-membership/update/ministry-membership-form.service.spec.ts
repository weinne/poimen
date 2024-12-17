import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ministry-membership.test-samples';

import { MinistryMembershipFormService } from './ministry-membership-form.service';

describe('MinistryMembership Form Service', () => {
  let service: MinistryMembershipFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MinistryMembershipFormService);
  });

  describe('Service methods', () => {
    describe('createMinistryMembershipFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMinistryMembershipFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            role: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            status: expect.any(Object),
            ministryGroup: expect.any(Object),
            member: expect.any(Object),
          }),
        );
      });

      it('passing IMinistryMembership should create a new form with FormGroup', () => {
        const formGroup = service.createMinistryMembershipFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            role: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            status: expect.any(Object),
            ministryGroup: expect.any(Object),
            member: expect.any(Object),
          }),
        );
      });
    });

    describe('getMinistryMembership', () => {
      it('should return NewMinistryMembership for default MinistryMembership initial value', () => {
        const formGroup = service.createMinistryMembershipFormGroup(sampleWithNewData);

        const ministryMembership = service.getMinistryMembership(formGroup) as any;

        expect(ministryMembership).toMatchObject(sampleWithNewData);
      });

      it('should return NewMinistryMembership for empty MinistryMembership initial value', () => {
        const formGroup = service.createMinistryMembershipFormGroup();

        const ministryMembership = service.getMinistryMembership(formGroup) as any;

        expect(ministryMembership).toMatchObject({});
      });

      it('should return IMinistryMembership', () => {
        const formGroup = service.createMinistryMembershipFormGroup(sampleWithRequiredData);

        const ministryMembership = service.getMinistryMembership(formGroup) as any;

        expect(ministryMembership).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMinistryMembership should not enable id FormControl', () => {
        const formGroup = service.createMinistryMembershipFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMinistryMembership should disable id FormControl', () => {
        const formGroup = service.createMinistryMembershipFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
