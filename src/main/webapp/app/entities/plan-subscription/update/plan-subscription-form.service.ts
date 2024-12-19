import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlanSubscription, NewPlanSubscription } from '../plan-subscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlanSubscription for edit and NewPlanSubscriptionFormGroupInput for create.
 */
type PlanSubscriptionFormGroupInput = IPlanSubscription | PartialWithRequiredKeyOf<NewPlanSubscription>;

type PlanSubscriptionFormDefaults = Pick<NewPlanSubscription, 'id'>;

type PlanSubscriptionFormGroupContent = {
  id: FormControl<IPlanSubscription['id'] | NewPlanSubscription['id']>;
  description: FormControl<IPlanSubscription['description']>;
  startDate: FormControl<IPlanSubscription['startDate']>;
  endDate: FormControl<IPlanSubscription['endDate']>;
  status: FormControl<IPlanSubscription['status']>;
  paymentProvider: FormControl<IPlanSubscription['paymentProvider']>;
  paymentStatus: FormControl<IPlanSubscription['paymentStatus']>;
  paymentReference: FormControl<IPlanSubscription['paymentReference']>;
  church: FormControl<IPlanSubscription['church']>;
  plan: FormControl<IPlanSubscription['plan']>;
  user: FormControl<IPlanSubscription['user']>;
};

export type PlanSubscriptionFormGroup = FormGroup<PlanSubscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlanSubscriptionFormService {
  createPlanSubscriptionFormGroup(planSubscription: PlanSubscriptionFormGroupInput = { id: null }): PlanSubscriptionFormGroup {
    const planSubscriptionRawValue = {
      ...this.getFormDefaults(),
      ...planSubscription,
    };
    return new FormGroup<PlanSubscriptionFormGroupContent>({
      id: new FormControl(
        { value: planSubscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(planSubscriptionRawValue.description, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(planSubscriptionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(planSubscriptionRawValue.endDate),
      status: new FormControl(planSubscriptionRawValue.status, {
        validators: [Validators.required],
      }),
      paymentProvider: new FormControl(planSubscriptionRawValue.paymentProvider, {
        validators: [Validators.required],
      }),
      paymentStatus: new FormControl(planSubscriptionRawValue.paymentStatus, {
        validators: [Validators.required],
      }),
      paymentReference: new FormControl(planSubscriptionRawValue.paymentReference),
      church: new FormControl(planSubscriptionRawValue.church),
      plan: new FormControl(planSubscriptionRawValue.plan),
      user: new FormControl(planSubscriptionRawValue.user),
    });
  }

  getPlanSubscription(form: PlanSubscriptionFormGroup): IPlanSubscription | NewPlanSubscription {
    return form.getRawValue() as IPlanSubscription | NewPlanSubscription;
  }

  resetForm(form: PlanSubscriptionFormGroup, planSubscription: PlanSubscriptionFormGroupInput): void {
    const planSubscriptionRawValue = { ...this.getFormDefaults(), ...planSubscription };
    form.reset(
      {
        ...planSubscriptionRawValue,
        id: { value: planSubscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlanSubscriptionFormDefaults {
    return {
      id: null,
    };
  }
}
