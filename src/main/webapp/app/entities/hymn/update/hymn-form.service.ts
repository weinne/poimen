import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IHymn, NewHymn } from '../hymn.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHymn for edit and NewHymnFormGroupInput for create.
 */
type HymnFormGroupInput = IHymn | PartialWithRequiredKeyOf<NewHymn>;

type HymnFormDefaults = Pick<NewHymn, 'id' | 'worshipEvents'>;

type HymnFormGroupContent = {
  id: FormControl<IHymn['id'] | NewHymn['id']>;
  title: FormControl<IHymn['title']>;
  author: FormControl<IHymn['author']>;
  hymnNumber: FormControl<IHymn['hymnNumber']>;
  lyrics: FormControl<IHymn['lyrics']>;
  worshipEvents: FormControl<IHymn['worshipEvents']>;
};

export type HymnFormGroup = FormGroup<HymnFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HymnFormService {
  createHymnFormGroup(hymn: HymnFormGroupInput = { id: null }): HymnFormGroup {
    const hymnRawValue = {
      ...this.getFormDefaults(),
      ...hymn,
    };
    return new FormGroup<HymnFormGroupContent>({
      id: new FormControl(
        { value: hymnRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(hymnRawValue.title, {
        validators: [Validators.required],
      }),
      author: new FormControl(hymnRawValue.author),
      hymnNumber: new FormControl(hymnRawValue.hymnNumber),
      lyrics: new FormControl(hymnRawValue.lyrics),
      worshipEvents: new FormControl(hymnRawValue.worshipEvents ?? []),
    });
  }

  getHymn(form: HymnFormGroup): IHymn | NewHymn {
    return form.getRawValue() as IHymn | NewHymn;
  }

  resetForm(form: HymnFormGroup, hymn: HymnFormGroupInput): void {
    const hymnRawValue = { ...this.getFormDefaults(), ...hymn };
    form.reset(
      {
        ...hymnRawValue,
        id: { value: hymnRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HymnFormDefaults {
    return {
      id: null,
      worshipEvents: [],
    };
  }
}
