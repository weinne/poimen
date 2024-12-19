import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

type MemberFormDefaults = Pick<NewMember, 'id' | 'playIns' | 'participateIns' | 'memberOfs'>;

type MemberFormGroupContent = {
  id: FormControl<IMember['id'] | NewMember['id']>;
  name: FormControl<IMember['name']>;
  photo: FormControl<IMember['photo']>;
  photoContentType: FormControl<IMember['photoContentType']>;
  email: FormControl<IMember['email']>;
  phoneNumber: FormControl<IMember['phoneNumber']>;
  dateOfBirth: FormControl<IMember['dateOfBirth']>;
  address: FormControl<IMember['address']>;
  city: FormControl<IMember['city']>;
  state: FormControl<IMember['state']>;
  zipCode: FormControl<IMember['zipCode']>;
  cityOfBirth: FormControl<IMember['cityOfBirth']>;
  previousReligion: FormControl<IMember['previousReligion']>;
  maritalStatus: FormControl<IMember['maritalStatus']>;
  spouseName: FormControl<IMember['spouseName']>;
  dateOfMarriage: FormControl<IMember['dateOfMarriage']>;
  status: FormControl<IMember['status']>;
  cpf: FormControl<IMember['cpf']>;
  rg: FormControl<IMember['rg']>;
  dateOfBaptism: FormControl<IMember['dateOfBaptism']>;
  churchOfBaptism: FormControl<IMember['churchOfBaptism']>;
  dateOfMembership: FormControl<IMember['dateOfMembership']>;
  typeOfMembership: FormControl<IMember['typeOfMembership']>;
  associationMeetingMinutes: FormControl<IMember['associationMeetingMinutes']>;
  dateOfDeath: FormControl<IMember['dateOfDeath']>;
  dateOfExit: FormControl<IMember['dateOfExit']>;
  exitDestination: FormControl<IMember['exitDestination']>;
  exitReason: FormControl<IMember['exitReason']>;
  exitMeetingMinutes: FormControl<IMember['exitMeetingMinutes']>;
  notes: FormControl<IMember['notes']>;
  church: FormControl<IMember['church']>;
  playIns: FormControl<IMember['playIns']>;
  participateIns: FormControl<IMember['participateIns']>;
  memberOfs: FormControl<IMember['memberOfs']>;
};

export type MemberFormGroup = FormGroup<MemberFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MemberFormService {
  createMemberFormGroup(member: MemberFormGroupInput = { id: null }): MemberFormGroup {
    const memberRawValue = {
      ...this.getFormDefaults(),
      ...member,
    };
    return new FormGroup<MemberFormGroupContent>({
      id: new FormControl(
        { value: memberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(memberRawValue.name, {
        validators: [Validators.required],
      }),
      photo: new FormControl(memberRawValue.photo),
      photoContentType: new FormControl(memberRawValue.photoContentType),
      email: new FormControl(memberRawValue.email),
      phoneNumber: new FormControl(memberRawValue.phoneNumber),
      dateOfBirth: new FormControl(memberRawValue.dateOfBirth, {
        validators: [Validators.required],
      }),
      address: new FormControl(memberRawValue.address),
      city: new FormControl(memberRawValue.city),
      state: new FormControl(memberRawValue.state),
      zipCode: new FormControl(memberRawValue.zipCode),
      cityOfBirth: new FormControl(memberRawValue.cityOfBirth),
      previousReligion: new FormControl(memberRawValue.previousReligion),
      maritalStatus: new FormControl(memberRawValue.maritalStatus, {
        validators: [Validators.required],
      }),
      spouseName: new FormControl(memberRawValue.spouseName),
      dateOfMarriage: new FormControl(memberRawValue.dateOfMarriage),
      status: new FormControl(memberRawValue.status, {
        validators: [Validators.required],
      }),
      cpf: new FormControl(memberRawValue.cpf, {
        validators: [Validators.required, Validators.pattern('^\\d{11}$')],
      }),
      rg: new FormControl(memberRawValue.rg, {
        validators: [Validators.required],
      }),
      dateOfBaptism: new FormControl(memberRawValue.dateOfBaptism),
      churchOfBaptism: new FormControl(memberRawValue.churchOfBaptism),
      dateOfMembership: new FormControl(memberRawValue.dateOfMembership),
      typeOfMembership: new FormControl(memberRawValue.typeOfMembership),
      associationMeetingMinutes: new FormControl(memberRawValue.associationMeetingMinutes),
      dateOfDeath: new FormControl(memberRawValue.dateOfDeath),
      dateOfExit: new FormControl(memberRawValue.dateOfExit),
      exitDestination: new FormControl(memberRawValue.exitDestination),
      exitReason: new FormControl(memberRawValue.exitReason),
      exitMeetingMinutes: new FormControl(memberRawValue.exitMeetingMinutes),
      notes: new FormControl(memberRawValue.notes),
      church: new FormControl(memberRawValue.church),
      playIns: new FormControl(memberRawValue.playIns ?? []),
      participateIns: new FormControl(memberRawValue.participateIns ?? []),
      memberOfs: new FormControl(memberRawValue.memberOfs ?? []),
    });
  }

  getMember(form: MemberFormGroup): IMember | NewMember {
    return form.getRawValue() as IMember | NewMember;
  }

  resetForm(form: MemberFormGroup, member: MemberFormGroupInput): void {
    const memberRawValue = { ...this.getFormDefaults(), ...member };
    form.reset(
      {
        ...memberRawValue,
        id: { value: memberRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MemberFormDefaults {
    return {
      id: null,
      playIns: [],
      participateIns: [],
      memberOfs: [],
    };
  }
}
