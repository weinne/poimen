import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMinistryMembership, NewMinistryMembership } from '../ministry-membership.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMinistryMembership for edit and NewMinistryMembershipFormGroupInput for create.
 */
type MinistryMembershipFormGroupInput = IMinistryMembership | PartialWithRequiredKeyOf<NewMinistryMembership>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMinistryMembership | NewMinistryMembership> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type MinistryMembershipFormRawValue = FormValueOf<IMinistryMembership>;

type NewMinistryMembershipFormRawValue = FormValueOf<NewMinistryMembership>;

type MinistryMembershipFormDefaults = Pick<NewMinistryMembership, 'id' | 'startDate' | 'endDate' | 'status'>;

type MinistryMembershipFormGroupContent = {
  id: FormControl<MinistryMembershipFormRawValue['id'] | NewMinistryMembership['id']>;
  role: FormControl<MinistryMembershipFormRawValue['role']>;
  startDate: FormControl<MinistryMembershipFormRawValue['startDate']>;
  endDate: FormControl<MinistryMembershipFormRawValue['endDate']>;
  status: FormControl<MinistryMembershipFormRawValue['status']>;
  ministryGroup: FormControl<MinistryMembershipFormRawValue['ministryGroup']>;
  member: FormControl<MinistryMembershipFormRawValue['member']>;
};

export type MinistryMembershipFormGroup = FormGroup<MinistryMembershipFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MinistryMembershipFormService {
  createMinistryMembershipFormGroup(ministryMembership: MinistryMembershipFormGroupInput = { id: null }): MinistryMembershipFormGroup {
    const ministryMembershipRawValue = this.convertMinistryMembershipToMinistryMembershipRawValue({
      ...this.getFormDefaults(),
      ...ministryMembership,
    });
    return new FormGroup<MinistryMembershipFormGroupContent>({
      id: new FormControl(
        { value: ministryMembershipRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      role: new FormControl(ministryMembershipRawValue.role, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(ministryMembershipRawValue.startDate),
      endDate: new FormControl(ministryMembershipRawValue.endDate),
      status: new FormControl(ministryMembershipRawValue.status),
      ministryGroup: new FormControl(ministryMembershipRawValue.ministryGroup),
      member: new FormControl(ministryMembershipRawValue.member),
    });
  }

  getMinistryMembership(form: MinistryMembershipFormGroup): IMinistryMembership | NewMinistryMembership {
    return this.convertMinistryMembershipRawValueToMinistryMembership(
      form.getRawValue() as MinistryMembershipFormRawValue | NewMinistryMembershipFormRawValue,
    );
  }

  resetForm(form: MinistryMembershipFormGroup, ministryMembership: MinistryMembershipFormGroupInput): void {
    const ministryMembershipRawValue = this.convertMinistryMembershipToMinistryMembershipRawValue({
      ...this.getFormDefaults(),
      ...ministryMembership,
    });
    form.reset(
      {
        ...ministryMembershipRawValue,
        id: { value: ministryMembershipRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MinistryMembershipFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
      status: false,
    };
  }

  private convertMinistryMembershipRawValueToMinistryMembership(
    rawMinistryMembership: MinistryMembershipFormRawValue | NewMinistryMembershipFormRawValue,
  ): IMinistryMembership | NewMinistryMembership {
    return {
      ...rawMinistryMembership,
      startDate: dayjs(rawMinistryMembership.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawMinistryMembership.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertMinistryMembershipToMinistryMembershipRawValue(
    ministryMembership: IMinistryMembership | (Partial<NewMinistryMembership> & MinistryMembershipFormDefaults),
  ): MinistryMembershipFormRawValue | PartialWithRequiredKeyOf<NewMinistryMembershipFormRawValue> {
    return {
      ...ministryMembership,
      startDate: ministryMembership.startDate ? ministryMembership.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: ministryMembership.endDate ? ministryMembership.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
