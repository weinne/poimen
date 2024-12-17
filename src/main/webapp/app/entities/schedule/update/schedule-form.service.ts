import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISchedule, NewSchedule } from '../schedule.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISchedule for edit and NewScheduleFormGroupInput for create.
 */
type ScheduleFormGroupInput = ISchedule | PartialWithRequiredKeyOf<NewSchedule>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISchedule | NewSchedule> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type ScheduleFormRawValue = FormValueOf<ISchedule>;

type NewScheduleFormRawValue = FormValueOf<NewSchedule>;

type ScheduleFormDefaults = Pick<NewSchedule, 'id' | 'startTime' | 'endTime' | 'members' | 'worshipEvents'>;

type ScheduleFormGroupContent = {
  id: FormControl<ScheduleFormRawValue['id'] | NewSchedule['id']>;
  roleType: FormControl<ScheduleFormRawValue['roleType']>;
  notes: FormControl<ScheduleFormRawValue['notes']>;
  startTime: FormControl<ScheduleFormRawValue['startTime']>;
  endTime: FormControl<ScheduleFormRawValue['endTime']>;
  members: FormControl<ScheduleFormRawValue['members']>;
  worshipEvents: FormControl<ScheduleFormRawValue['worshipEvents']>;
};

export type ScheduleFormGroup = FormGroup<ScheduleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScheduleFormService {
  createScheduleFormGroup(schedule: ScheduleFormGroupInput = { id: null }): ScheduleFormGroup {
    const scheduleRawValue = this.convertScheduleToScheduleRawValue({
      ...this.getFormDefaults(),
      ...schedule,
    });
    return new FormGroup<ScheduleFormGroupContent>({
      id: new FormControl(
        { value: scheduleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      roleType: new FormControl(scheduleRawValue.roleType, {
        validators: [Validators.required],
      }),
      notes: new FormControl(scheduleRawValue.notes),
      startTime: new FormControl(scheduleRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(scheduleRawValue.endTime),
      members: new FormControl(scheduleRawValue.members ?? []),
      worshipEvents: new FormControl(scheduleRawValue.worshipEvents ?? []),
    });
  }

  getSchedule(form: ScheduleFormGroup): ISchedule | NewSchedule {
    return this.convertScheduleRawValueToSchedule(form.getRawValue() as ScheduleFormRawValue | NewScheduleFormRawValue);
  }

  resetForm(form: ScheduleFormGroup, schedule: ScheduleFormGroupInput): void {
    const scheduleRawValue = this.convertScheduleToScheduleRawValue({ ...this.getFormDefaults(), ...schedule });
    form.reset(
      {
        ...scheduleRawValue,
        id: { value: scheduleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ScheduleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
      members: [],
      worshipEvents: [],
    };
  }

  private convertScheduleRawValueToSchedule(rawSchedule: ScheduleFormRawValue | NewScheduleFormRawValue): ISchedule | NewSchedule {
    return {
      ...rawSchedule,
      startTime: dayjs(rawSchedule.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawSchedule.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertScheduleToScheduleRawValue(
    schedule: ISchedule | (Partial<NewSchedule> & ScheduleFormDefaults),
  ): ScheduleFormRawValue | PartialWithRequiredKeyOf<NewScheduleFormRawValue> {
    return {
      ...schedule,
      startTime: schedule.startTime ? schedule.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: schedule.endTime ? schedule.endTime.format(DATE_TIME_FORMAT) : undefined,
      members: schedule.members ?? [],
      worshipEvents: schedule.worshipEvents ?? [],
    };
  }
}
