import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICounselingSession, NewCounselingSession } from '../counseling-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICounselingSession for edit and NewCounselingSessionFormGroupInput for create.
 */
type CounselingSessionFormGroupInput = ICounselingSession | PartialWithRequiredKeyOf<NewCounselingSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICounselingSession | NewCounselingSession> = Omit<T, 'date'> & {
  date?: string | null;
};

type CounselingSessionFormRawValue = FormValueOf<ICounselingSession>;

type NewCounselingSessionFormRawValue = FormValueOf<NewCounselingSession>;

type CounselingSessionFormDefaults = Pick<NewCounselingSession, 'id' | 'date'>;

type CounselingSessionFormGroupContent = {
  id: FormControl<CounselingSessionFormRawValue['id'] | NewCounselingSession['id']>;
  date: FormControl<CounselingSessionFormRawValue['date']>;
  notes: FormControl<CounselingSessionFormRawValue['notes']>;
  church: FormControl<CounselingSessionFormRawValue['church']>;
  member: FormControl<CounselingSessionFormRawValue['member']>;
  user: FormControl<CounselingSessionFormRawValue['user']>;
};

export type CounselingSessionFormGroup = FormGroup<CounselingSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CounselingSessionFormService {
  createCounselingSessionFormGroup(counselingSession: CounselingSessionFormGroupInput = { id: null }): CounselingSessionFormGroup {
    const counselingSessionRawValue = this.convertCounselingSessionToCounselingSessionRawValue({
      ...this.getFormDefaults(),
      ...counselingSession,
    });
    return new FormGroup<CounselingSessionFormGroupContent>({
      id: new FormControl(
        { value: counselingSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(counselingSessionRawValue.date, {
        validators: [Validators.required],
      }),
      notes: new FormControl(counselingSessionRawValue.notes),
      church: new FormControl(counselingSessionRawValue.church),
      member: new FormControl(counselingSessionRawValue.member),
      user: new FormControl(counselingSessionRawValue.user),
    });
  }

  getCounselingSession(form: CounselingSessionFormGroup): ICounselingSession | NewCounselingSession {
    return this.convertCounselingSessionRawValueToCounselingSession(
      form.getRawValue() as CounselingSessionFormRawValue | NewCounselingSessionFormRawValue,
    );
  }

  resetForm(form: CounselingSessionFormGroup, counselingSession: CounselingSessionFormGroupInput): void {
    const counselingSessionRawValue = this.convertCounselingSessionToCounselingSessionRawValue({
      ...this.getFormDefaults(),
      ...counselingSession,
    });
    form.reset(
      {
        ...counselingSessionRawValue,
        id: { value: counselingSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CounselingSessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertCounselingSessionRawValueToCounselingSession(
    rawCounselingSession: CounselingSessionFormRawValue | NewCounselingSessionFormRawValue,
  ): ICounselingSession | NewCounselingSession {
    return {
      ...rawCounselingSession,
      date: dayjs(rawCounselingSession.date, DATE_TIME_FORMAT),
    };
  }

  private convertCounselingSessionToCounselingSessionRawValue(
    counselingSession: ICounselingSession | (Partial<NewCounselingSession> & CounselingSessionFormDefaults),
  ): CounselingSessionFormRawValue | PartialWithRequiredKeyOf<NewCounselingSessionFormRawValue> {
    return {
      ...counselingSession,
      date: counselingSession.date ? counselingSession.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
