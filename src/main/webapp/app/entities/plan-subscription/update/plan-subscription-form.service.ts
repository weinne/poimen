import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPlanSubscription | NewPlanSubscription> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type PlanSubscriptionFormRawValue = FormValueOf<IPlanSubscription>;

type NewPlanSubscriptionFormRawValue = FormValueOf<NewPlanSubscription>;

type PlanSubscriptionFormDefaults = Pick<NewPlanSubscription, 'id' | 'startDate' | 'endDate' | 'active'>;

type PlanSubscriptionFormGroupContent = {
  id: FormControl<PlanSubscriptionFormRawValue['id'] | NewPlanSubscription['id']>;
  startDate: FormControl<PlanSubscriptionFormRawValue['startDate']>;
  endDate: FormControl<PlanSubscriptionFormRawValue['endDate']>;
  active: FormControl<PlanSubscriptionFormRawValue['active']>;
  planName: FormControl<PlanSubscriptionFormRawValue['planName']>;
  plan: FormControl<PlanSubscriptionFormRawValue['plan']>;
  church: FormControl<PlanSubscriptionFormRawValue['church']>;
  user: FormControl<PlanSubscriptionFormRawValue['user']>;
};

export type PlanSubscriptionFormGroup = FormGroup<PlanSubscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlanSubscriptionFormService {
  createPlanSubscriptionFormGroup(planSubscription: PlanSubscriptionFormGroupInput = { id: null }): PlanSubscriptionFormGroup {
    const planSubscriptionRawValue = this.convertPlanSubscriptionToPlanSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...planSubscription,
    });
    return new FormGroup<PlanSubscriptionFormGroupContent>({
      id: new FormControl(
        { value: planSubscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      startDate: new FormControl(planSubscriptionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(planSubscriptionRawValue.endDate),
      active: new FormControl(planSubscriptionRawValue.active, {
        validators: [Validators.required],
      }),
      planName: new FormControl(planSubscriptionRawValue.planName, {
        validators: [Validators.required],
      }),
      plan: new FormControl(planSubscriptionRawValue.plan),
      church: new FormControl(planSubscriptionRawValue.church),
      user: new FormControl(planSubscriptionRawValue.user),
    });
  }

  getPlanSubscription(form: PlanSubscriptionFormGroup): IPlanSubscription | NewPlanSubscription {
    return this.convertPlanSubscriptionRawValueToPlanSubscription(
      form.getRawValue() as PlanSubscriptionFormRawValue | NewPlanSubscriptionFormRawValue,
    );
  }

  resetForm(form: PlanSubscriptionFormGroup, planSubscription: PlanSubscriptionFormGroupInput): void {
    const planSubscriptionRawValue = this.convertPlanSubscriptionToPlanSubscriptionRawValue({
      ...this.getFormDefaults(),
      ...planSubscription,
    });
    form.reset(
      {
        ...planSubscriptionRawValue,
        id: { value: planSubscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlanSubscriptionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      active: false,
    };
  }

  private convertPlanSubscriptionRawValueToPlanSubscription(
    rawPlanSubscription: PlanSubscriptionFormRawValue | NewPlanSubscriptionFormRawValue,
  ): IPlanSubscription | NewPlanSubscription {
    return {
      ...rawPlanSubscription,
      startDate: dayjs(rawPlanSubscription.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawPlanSubscription.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertPlanSubscriptionToPlanSubscriptionRawValue(
    planSubscription: IPlanSubscription | (Partial<NewPlanSubscription> & PlanSubscriptionFormDefaults),
  ): PlanSubscriptionFormRawValue | PartialWithRequiredKeyOf<NewPlanSubscriptionFormRawValue> {
    return {
      ...planSubscription,
      startDate: planSubscription.startDate ? planSubscription.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: planSubscription.endDate ? planSubscription.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
