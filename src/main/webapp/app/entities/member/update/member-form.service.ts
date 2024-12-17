import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMember, NewMember } from '../member.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMember for edit and NewMemberFormGroupInput for create.
 */
type MemberFormGroupInput = IMember | PartialWithRequiredKeyOf<NewMember>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMember | NewMember> = Omit<T, 'dateOfBirth'> & {
  dateOfBirth?: string | null;
};

type MemberFormRawValue = FormValueOf<IMember>;

type NewMemberFormRawValue = FormValueOf<NewMember>;

type MemberFormDefaults = Pick<NewMember, 'id' | 'dateOfBirth' | 'schedules'>;

type MemberFormGroupContent = {
  id: FormControl<MemberFormRawValue['id'] | NewMember['id']>;
  firstName: FormControl<MemberFormRawValue['firstName']>;
  lastName: FormControl<MemberFormRawValue['lastName']>;
  email: FormControl<MemberFormRawValue['email']>;
  phoneNumber: FormControl<MemberFormRawValue['phoneNumber']>;
  dateOfBirth: FormControl<MemberFormRawValue['dateOfBirth']>;
  address: FormControl<MemberFormRawValue['address']>;
  church: FormControl<MemberFormRawValue['church']>;
  schedules: FormControl<MemberFormRawValue['schedules']>;
};

export type MemberFormGroup = FormGroup<MemberFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MemberFormService {
  createMemberFormGroup(member: MemberFormGroupInput = { id: null }): MemberFormGroup {
    const memberRawValue = this.convertMemberToMemberRawValue({
      ...this.getFormDefaults(),
      ...member,
    });
    return new FormGroup<MemberFormGroupContent>({
      id: new FormControl(
        { value: memberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(memberRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(memberRawValue.lastName, {
        validators: [Validators.required],
      }),
      email: new FormControl(memberRawValue.email),
      phoneNumber: new FormControl(memberRawValue.phoneNumber),
      dateOfBirth: new FormControl(memberRawValue.dateOfBirth),
      address: new FormControl(memberRawValue.address),
      church: new FormControl(memberRawValue.church),
      schedules: new FormControl(memberRawValue.schedules ?? []),
    });
  }

  getMember(form: MemberFormGroup): IMember | NewMember {
    return this.convertMemberRawValueToMember(form.getRawValue() as MemberFormRawValue | NewMemberFormRawValue);
  }

  resetForm(form: MemberFormGroup, member: MemberFormGroupInput): void {
    const memberRawValue = this.convertMemberToMemberRawValue({ ...this.getFormDefaults(), ...member });
    form.reset(
      {
        ...memberRawValue,
        id: { value: memberRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MemberFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateOfBirth: currentTime,
      schedules: [],
    };
  }

  private convertMemberRawValueToMember(rawMember: MemberFormRawValue | NewMemberFormRawValue): IMember | NewMember {
    return {
      ...rawMember,
      dateOfBirth: dayjs(rawMember.dateOfBirth, DATE_TIME_FORMAT),
    };
  }

  private convertMemberToMemberRawValue(
    member: IMember | (Partial<NewMember> & MemberFormDefaults),
  ): MemberFormRawValue | PartialWithRequiredKeyOf<NewMemberFormRawValue> {
    return {
      ...member,
      dateOfBirth: member.dateOfBirth ? member.dateOfBirth.format(DATE_TIME_FORMAT) : undefined,
      schedules: member.schedules ?? [],
    };
  }
}
