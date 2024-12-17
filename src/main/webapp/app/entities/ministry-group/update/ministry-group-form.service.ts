import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMinistryGroup, NewMinistryGroup } from '../ministry-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMinistryGroup for edit and NewMinistryGroupFormGroupInput for create.
 */
type MinistryGroupFormGroupInput = IMinistryGroup | PartialWithRequiredKeyOf<NewMinistryGroup>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMinistryGroup | NewMinistryGroup> = Omit<T, 'establishedDate'> & {
  establishedDate?: string | null;
};

type MinistryGroupFormRawValue = FormValueOf<IMinistryGroup>;

type NewMinistryGroupFormRawValue = FormValueOf<NewMinistryGroup>;

type MinistryGroupFormDefaults = Pick<NewMinistryGroup, 'id' | 'establishedDate'>;

type MinistryGroupFormGroupContent = {
  id: FormControl<MinistryGroupFormRawValue['id'] | NewMinistryGroup['id']>;
  name: FormControl<MinistryGroupFormRawValue['name']>;
  description: FormControl<MinistryGroupFormRawValue['description']>;
  establishedDate: FormControl<MinistryGroupFormRawValue['establishedDate']>;
  leader: FormControl<MinistryGroupFormRawValue['leader']>;
  type: FormControl<MinistryGroupFormRawValue['type']>;
  church: FormControl<MinistryGroupFormRawValue['church']>;
};

export type MinistryGroupFormGroup = FormGroup<MinistryGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MinistryGroupFormService {
  createMinistryGroupFormGroup(ministryGroup: MinistryGroupFormGroupInput = { id: null }): MinistryGroupFormGroup {
    const ministryGroupRawValue = this.convertMinistryGroupToMinistryGroupRawValue({
      ...this.getFormDefaults(),
      ...ministryGroup,
    });
    return new FormGroup<MinistryGroupFormGroupContent>({
      id: new FormControl(
        { value: ministryGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(ministryGroupRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(ministryGroupRawValue.description),
      establishedDate: new FormControl(ministryGroupRawValue.establishedDate),
      leader: new FormControl(ministryGroupRawValue.leader),
      type: new FormControl(ministryGroupRawValue.type, {
        validators: [Validators.required],
      }),
      church: new FormControl(ministryGroupRawValue.church),
    });
  }

  getMinistryGroup(form: MinistryGroupFormGroup): IMinistryGroup | NewMinistryGroup {
    return this.convertMinistryGroupRawValueToMinistryGroup(form.getRawValue() as MinistryGroupFormRawValue | NewMinistryGroupFormRawValue);
  }

  resetForm(form: MinistryGroupFormGroup, ministryGroup: MinistryGroupFormGroupInput): void {
    const ministryGroupRawValue = this.convertMinistryGroupToMinistryGroupRawValue({ ...this.getFormDefaults(), ...ministryGroup });
    form.reset(
      {
        ...ministryGroupRawValue,
        id: { value: ministryGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MinistryGroupFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      establishedDate: currentTime,
    };
  }

  private convertMinistryGroupRawValueToMinistryGroup(
    rawMinistryGroup: MinistryGroupFormRawValue | NewMinistryGroupFormRawValue,
  ): IMinistryGroup | NewMinistryGroup {
    return {
      ...rawMinistryGroup,
      establishedDate: dayjs(rawMinistryGroup.establishedDate, DATE_TIME_FORMAT),
    };
  }

  private convertMinistryGroupToMinistryGroupRawValue(
    ministryGroup: IMinistryGroup | (Partial<NewMinistryGroup> & MinistryGroupFormDefaults),
  ): MinistryGroupFormRawValue | PartialWithRequiredKeyOf<NewMinistryGroupFormRawValue> {
    return {
      ...ministryGroup,
      establishedDate: ministryGroup.establishedDate ? ministryGroup.establishedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
