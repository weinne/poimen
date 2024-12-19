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

type HymnFormDefaults = Pick<NewHymn, 'id' | 'services'>;

type HymnFormGroupContent = {
  id: FormControl<IHymn['id'] | NewHymn['id']>;
  title: FormControl<IHymn['title']>;
  lyricsAuthor: FormControl<IHymn['lyricsAuthor']>;
  musicAuthor: FormControl<IHymn['musicAuthor']>;
  hymnary: FormControl<IHymn['hymnary']>;
  hymnNumber: FormControl<IHymn['hymnNumber']>;
  link: FormControl<IHymn['link']>;
  youtubeLink: FormControl<IHymn['youtubeLink']>;
  sheetMusic: FormControl<IHymn['sheetMusic']>;
  sheetMusicContentType: FormControl<IHymn['sheetMusicContentType']>;
  midi: FormControl<IHymn['midi']>;
  midiContentType: FormControl<IHymn['midiContentType']>;
  tone: FormControl<IHymn['tone']>;
  lyrics: FormControl<IHymn['lyrics']>;
  services: FormControl<IHymn['services']>;
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
      lyricsAuthor: new FormControl(hymnRawValue.lyricsAuthor),
      musicAuthor: new FormControl(hymnRawValue.musicAuthor),
      hymnary: new FormControl(hymnRawValue.hymnary),
      hymnNumber: new FormControl(hymnRawValue.hymnNumber),
      link: new FormControl(hymnRawValue.link),
      youtubeLink: new FormControl(hymnRawValue.youtubeLink),
      sheetMusic: new FormControl(hymnRawValue.sheetMusic),
      sheetMusicContentType: new FormControl(hymnRawValue.sheetMusicContentType),
      midi: new FormControl(hymnRawValue.midi),
      midiContentType: new FormControl(hymnRawValue.midiContentType),
      tone: new FormControl(hymnRawValue.tone, {
        validators: [Validators.maxLength(5)],
      }),
      lyrics: new FormControl(hymnRawValue.lyrics),
      services: new FormControl(hymnRawValue.services ?? []),
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
      services: [],
    };
  }
}
