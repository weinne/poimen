import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../plan-subscription.test-samples';

import { PlanSubscriptionFormService } from './plan-subscription-form.service';

describe('PlanSubscription Form Service', () => {
  let service: PlanSubscriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlanSubscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createPlanSubscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlanSubscriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            status: expect.any(Object),
            paymentProvider: expect.any(Object),
            paymentStatus: expect.any(Object),
            paymentReference: expect.any(Object),
            church: expect.any(Object),
            plan: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IPlanSubscription should create a new form with FormGroup', () => {
        const formGroup = service.createPlanSubscriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            status: expect.any(Object),
            paymentProvider: expect.any(Object),
            paymentStatus: expect.any(Object),
            paymentReference: expect.any(Object),
            church: expect.any(Object),
            plan: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlanSubscription', () => {
      it('should return NewPlanSubscription for default PlanSubscription initial value', () => {
        const formGroup = service.createPlanSubscriptionFormGroup(sampleWithNewData);

        const planSubscription = service.getPlanSubscription(formGroup) as any;

        expect(planSubscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlanSubscription for empty PlanSubscription initial value', () => {
        const formGroup = service.createPlanSubscriptionFormGroup();

        const planSubscription = service.getPlanSubscription(formGroup) as any;

        expect(planSubscription).toMatchObject({});
      });

      it('should return IPlanSubscription', () => {
        const formGroup = service.createPlanSubscriptionFormGroup(sampleWithRequiredData);

        const planSubscription = service.getPlanSubscription(formGroup) as any;

        expect(planSubscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlanSubscription should not enable id FormControl', () => {
        const formGroup = service.createPlanSubscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlanSubscription should disable id FormControl', () => {
        const formGroup = service.createPlanSubscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
