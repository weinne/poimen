import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChurch, NewChurch } from '../church.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChurch for edit and NewChurchFormGroupInput for create.
 */
type ChurchFormGroupInput = IChurch | PartialWithRequiredKeyOf<NewChurch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IChurch | NewChurch> = Omit<T, 'dateFoundation'> & {
  dateFoundation?: string | null;
};

type ChurchFormRawValue = FormValueOf<IChurch>;

type NewChurchFormRawValue = FormValueOf<NewChurch>;

type ChurchFormDefaults = Pick<NewChurch, 'id' | 'dateFoundation' | 'users'>;

type ChurchFormGroupContent = {
  id: FormControl<ChurchFormRawValue['id'] | NewChurch['id']>;
  name: FormControl<ChurchFormRawValue['name']>;
  cnpj: FormControl<ChurchFormRawValue['cnpj']>;
  address: FormControl<ChurchFormRawValue['address']>;
  city: FormControl<ChurchFormRawValue['city']>;
  dateFoundation: FormControl<ChurchFormRawValue['dateFoundation']>;
  users: FormControl<ChurchFormRawValue['users']>;
};

export type ChurchFormGroup = FormGroup<ChurchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChurchFormService {
  createChurchFormGroup(church: ChurchFormGroupInput = { id: null }): ChurchFormGroup {
    const churchRawValue = this.convertChurchToChurchRawValue({
      ...this.getFormDefaults(),
      ...church,
    });
    return new FormGroup<ChurchFormGroupContent>({
      id: new FormControl(
        { value: churchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(churchRawValue.name, {
        validators: [Validators.required],
      }),
      cnpj: new FormControl(churchRawValue.cnpj, {
        validators: [Validators.required],
      }),
      address: new FormControl(churchRawValue.address, {
        validators: [Validators.required],
      }),
      city: new FormControl(churchRawValue.city, {
        validators: [Validators.required],
      }),
      dateFoundation: new FormControl(churchRawValue.dateFoundation, {
        validators: [Validators.required],
      }),
      users: new FormControl(churchRawValue.users ?? []),
    });
  }

  getChurch(form: ChurchFormGroup): IChurch | NewChurch {
    return this.convertChurchRawValueToChurch(form.getRawValue() as ChurchFormRawValue | NewChurchFormRawValue);
  }

  resetForm(form: ChurchFormGroup, church: ChurchFormGroupInput): void {
    const churchRawValue = this.convertChurchToChurchRawValue({ ...this.getFormDefaults(), ...church });
    form.reset(
      {
        ...churchRawValue,
        id: { value: churchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChurchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateFoundation: currentTime,
      users: [],
    };
  }

  private convertChurchRawValueToChurch(rawChurch: ChurchFormRawValue | NewChurchFormRawValue): IChurch | NewChurch {
    return {
      ...rawChurch,
      dateFoundation: dayjs(rawChurch.dateFoundation, DATE_TIME_FORMAT),
    };
  }

  private convertChurchToChurchRawValue(
    church: IChurch | (Partial<NewChurch> & ChurchFormDefaults),
  ): ChurchFormRawValue | PartialWithRequiredKeyOf<NewChurchFormRawValue> {
    return {
      ...church,
      dateFoundation: church.dateFoundation ? church.dateFoundation.format(DATE_TIME_FORMAT) : undefined,
      users: church.users ?? [],
    };
  }
}
