import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

type ChurchFormDefaults = Pick<NewChurch, 'id'>;

type ChurchFormGroupContent = {
  id: FormControl<IChurch['id'] | NewChurch['id']>;
  name: FormControl<IChurch['name']>;
  cnpj: FormControl<IChurch['cnpj']>;
  address: FormControl<IChurch['address']>;
  city: FormControl<IChurch['city']>;
  dateFoundation: FormControl<IChurch['dateFoundation']>;
  phone: FormControl<IChurch['phone']>;
  email: FormControl<IChurch['email']>;
  website: FormControl<IChurch['website']>;
  facebook: FormControl<IChurch['facebook']>;
  instagram: FormControl<IChurch['instagram']>;
  twitter: FormControl<IChurch['twitter']>;
  youtube: FormControl<IChurch['youtube']>;
  about: FormControl<IChurch['about']>;
};

export type ChurchFormGroup = FormGroup<ChurchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChurchFormService {
  createChurchFormGroup(church: ChurchFormGroupInput = { id: null }): ChurchFormGroup {
    const churchRawValue = {
      ...this.getFormDefaults(),
      ...church,
    };
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
        validators: [Validators.required, Validators.pattern('^\\d{14}$')],
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
      phone: new FormControl(churchRawValue.phone),
      email: new FormControl(churchRawValue.email),
      website: new FormControl(churchRawValue.website),
      facebook: new FormControl(churchRawValue.facebook),
      instagram: new FormControl(churchRawValue.instagram),
      twitter: new FormControl(churchRawValue.twitter),
      youtube: new FormControl(churchRawValue.youtube),
      about: new FormControl(churchRawValue.about),
    });
  }

  getChurch(form: ChurchFormGroup): IChurch | NewChurch {
    return form.getRawValue() as IChurch | NewChurch;
  }

  resetForm(form: ChurchFormGroup, church: ChurchFormGroupInput): void {
    const churchRawValue = { ...this.getFormDefaults(), ...church };
    form.reset(
      {
        ...churchRawValue,
        id: { value: churchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChurchFormDefaults {
    return {
      id: null,
    };
  }
}
