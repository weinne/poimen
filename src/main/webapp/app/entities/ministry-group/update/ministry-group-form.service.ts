import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

type MinistryGroupFormDefaults = Pick<NewMinistryGroup, 'id' | 'members'>;

type MinistryGroupFormGroupContent = {
  id: FormControl<IMinistryGroup['id'] | NewMinistryGroup['id']>;
  name: FormControl<IMinistryGroup['name']>;
  description: FormControl<IMinistryGroup['description']>;
  establishedDate: FormControl<IMinistryGroup['establishedDate']>;
  type: FormControl<IMinistryGroup['type']>;
  church: FormControl<IMinistryGroup['church']>;
  president: FormControl<IMinistryGroup['president']>;
  supervisor: FormControl<IMinistryGroup['supervisor']>;
  members: FormControl<IMinistryGroup['members']>;
};

export type MinistryGroupFormGroup = FormGroup<MinistryGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MinistryGroupFormService {
  createMinistryGroupFormGroup(ministryGroup: MinistryGroupFormGroupInput = { id: null }): MinistryGroupFormGroup {
    const ministryGroupRawValue = {
      ...this.getFormDefaults(),
      ...ministryGroup,
    };
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
      type: new FormControl(ministryGroupRawValue.type, {
        validators: [Validators.required],
      }),
      church: new FormControl(ministryGroupRawValue.church),
      president: new FormControl(ministryGroupRawValue.president),
      supervisor: new FormControl(ministryGroupRawValue.supervisor),
      members: new FormControl(ministryGroupRawValue.members ?? []),
    });
  }

  getMinistryGroup(form: MinistryGroupFormGroup): IMinistryGroup | NewMinistryGroup {
    return form.getRawValue() as IMinistryGroup | NewMinistryGroup;
  }

  resetForm(form: MinistryGroupFormGroup, ministryGroup: MinistryGroupFormGroupInput): void {
    const ministryGroupRawValue = { ...this.getFormDefaults(), ...ministryGroup };
    form.reset(
      {
        ...ministryGroupRawValue,
        id: { value: ministryGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MinistryGroupFormDefaults {
    return {
      id: null,
      members: [],
    };
  }
}
