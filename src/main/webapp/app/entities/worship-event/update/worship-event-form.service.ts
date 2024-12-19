import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorshipEvent, NewWorshipEvent } from '../worship-event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorshipEvent for edit and NewWorshipEventFormGroupInput for create.
 */
type WorshipEventFormGroupInput = IWorshipEvent | PartialWithRequiredKeyOf<NewWorshipEvent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWorshipEvent | NewWorshipEvent> = Omit<T, 'date'> & {
  date?: string | null;
};

type WorshipEventFormRawValue = FormValueOf<IWorshipEvent>;

type NewWorshipEventFormRawValue = FormValueOf<NewWorshipEvent>;

type WorshipEventFormDefaults = Pick<NewWorshipEvent, 'id' | 'date' | 'hymns' | 'musicians' | 'participants'>;

type WorshipEventFormGroupContent = {
  id: FormControl<WorshipEventFormRawValue['id'] | NewWorshipEvent['id']>;
  date: FormControl<WorshipEventFormRawValue['date']>;
  title: FormControl<WorshipEventFormRawValue['title']>;
  guestPreacher: FormControl<WorshipEventFormRawValue['guestPreacher']>;
  description: FormControl<WorshipEventFormRawValue['description']>;
  callToWorshipText: FormControl<WorshipEventFormRawValue['callToWorshipText']>;
  confessionOfSinText: FormControl<WorshipEventFormRawValue['confessionOfSinText']>;
  assuranceOfPardonText: FormControl<WorshipEventFormRawValue['assuranceOfPardonText']>;
  lordSupperText: FormControl<WorshipEventFormRawValue['lordSupperText']>;
  benedictionText: FormControl<WorshipEventFormRawValue['benedictionText']>;
  confessionalText: FormControl<WorshipEventFormRawValue['confessionalText']>;
  sermonText: FormControl<WorshipEventFormRawValue['sermonText']>;
  sermonFile: FormControl<WorshipEventFormRawValue['sermonFile']>;
  sermonFileContentType: FormControl<WorshipEventFormRawValue['sermonFileContentType']>;
  sermonLink: FormControl<WorshipEventFormRawValue['sermonLink']>;
  youtubeLink: FormControl<WorshipEventFormRawValue['youtubeLink']>;
  bulletinFile: FormControl<WorshipEventFormRawValue['bulletinFile']>;
  bulletinFileContentType: FormControl<WorshipEventFormRawValue['bulletinFileContentType']>;
  worshipType: FormControl<WorshipEventFormRawValue['worshipType']>;
  church: FormControl<WorshipEventFormRawValue['church']>;
  preacher: FormControl<WorshipEventFormRawValue['preacher']>;
  liturgist: FormControl<WorshipEventFormRawValue['liturgist']>;
  hymns: FormControl<WorshipEventFormRawValue['hymns']>;
  musicians: FormControl<WorshipEventFormRawValue['musicians']>;
  participants: FormControl<WorshipEventFormRawValue['participants']>;
};

export type WorshipEventFormGroup = FormGroup<WorshipEventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorshipEventFormService {
  createWorshipEventFormGroup(worshipEvent: WorshipEventFormGroupInput = { id: null }): WorshipEventFormGroup {
    const worshipEventRawValue = this.convertWorshipEventToWorshipEventRawValue({
      ...this.getFormDefaults(),
      ...worshipEvent,
    });
    return new FormGroup<WorshipEventFormGroupContent>({
      id: new FormControl(
        { value: worshipEventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(worshipEventRawValue.date, {
        validators: [Validators.required],
      }),
      title: new FormControl(worshipEventRawValue.title),
      guestPreacher: new FormControl(worshipEventRawValue.guestPreacher),
      description: new FormControl(worshipEventRawValue.description),
      callToWorshipText: new FormControl(worshipEventRawValue.callToWorshipText),
      confessionOfSinText: new FormControl(worshipEventRawValue.confessionOfSinText),
      assuranceOfPardonText: new FormControl(worshipEventRawValue.assuranceOfPardonText),
      lordSupperText: new FormControl(worshipEventRawValue.lordSupperText),
      benedictionText: new FormControl(worshipEventRawValue.benedictionText),
      confessionalText: new FormControl(worshipEventRawValue.confessionalText),
      sermonText: new FormControl(worshipEventRawValue.sermonText),
      sermonFile: new FormControl(worshipEventRawValue.sermonFile),
      sermonFileContentType: new FormControl(worshipEventRawValue.sermonFileContentType),
      sermonLink: new FormControl(worshipEventRawValue.sermonLink),
      youtubeLink: new FormControl(worshipEventRawValue.youtubeLink),
      bulletinFile: new FormControl(worshipEventRawValue.bulletinFile),
      bulletinFileContentType: new FormControl(worshipEventRawValue.bulletinFileContentType),
      worshipType: new FormControl(worshipEventRawValue.worshipType, {
        validators: [Validators.required],
      }),
      church: new FormControl(worshipEventRawValue.church),
      preacher: new FormControl(worshipEventRawValue.preacher),
      liturgist: new FormControl(worshipEventRawValue.liturgist),
      hymns: new FormControl(worshipEventRawValue.hymns ?? []),
      musicians: new FormControl(worshipEventRawValue.musicians ?? []),
      participants: new FormControl(worshipEventRawValue.participants ?? []),
    });
  }

  getWorshipEvent(form: WorshipEventFormGroup): IWorshipEvent | NewWorshipEvent {
    return this.convertWorshipEventRawValueToWorshipEvent(form.getRawValue() as WorshipEventFormRawValue | NewWorshipEventFormRawValue);
  }

  resetForm(form: WorshipEventFormGroup, worshipEvent: WorshipEventFormGroupInput): void {
    const worshipEventRawValue = this.convertWorshipEventToWorshipEventRawValue({ ...this.getFormDefaults(), ...worshipEvent });
    form.reset(
      {
        ...worshipEventRawValue,
        id: { value: worshipEventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorshipEventFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      hymns: [],
      musicians: [],
      participants: [],
    };
  }

  private convertWorshipEventRawValueToWorshipEvent(
    rawWorshipEvent: WorshipEventFormRawValue | NewWorshipEventFormRawValue,
  ): IWorshipEvent | NewWorshipEvent {
    return {
      ...rawWorshipEvent,
      date: dayjs(rawWorshipEvent.date, DATE_TIME_FORMAT),
    };
  }

  private convertWorshipEventToWorshipEventRawValue(
    worshipEvent: IWorshipEvent | (Partial<NewWorshipEvent> & WorshipEventFormDefaults),
  ): WorshipEventFormRawValue | PartialWithRequiredKeyOf<NewWorshipEventFormRawValue> {
    return {
      ...worshipEvent,
      date: worshipEvent.date ? worshipEvent.date.format(DATE_TIME_FORMAT) : undefined,
      hymns: worshipEvent.hymns ?? [],
      musicians: worshipEvent.musicians ?? [],
      participants: worshipEvent.participants ?? [],
    };
  }
}
