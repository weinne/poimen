import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../member.test-samples';

import { MemberFormService } from './member-form.service';

describe('Member Form Service', () => {
  let service: MemberFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MemberFormService);
  });

  describe('Service methods', () => {
    describe('createMemberFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMemberFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            photo: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            dateOfBirth: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            state: expect.any(Object),
            zipCode: expect.any(Object),
            cityOfBirth: expect.any(Object),
            previousReligion: expect.any(Object),
            maritalStatus: expect.any(Object),
            spouseName: expect.any(Object),
            dateOfMarriage: expect.any(Object),
            status: expect.any(Object),
            cpf: expect.any(Object),
            rg: expect.any(Object),
            dateOfBaptism: expect.any(Object),
            churchOfBaptism: expect.any(Object),
            dateOfMembership: expect.any(Object),
            typeOfMembership: expect.any(Object),
            associationMeetingMinutes: expect.any(Object),
            dateOfDeath: expect.any(Object),
            dateOfExit: expect.any(Object),
            exitDestination: expect.any(Object),
            exitReason: expect.any(Object),
            exitMeetingMinutes: expect.any(Object),
            notes: expect.any(Object),
            church: expect.any(Object),
            playIns: expect.any(Object),
            participateIns: expect.any(Object),
            memberOfs: expect.any(Object),
          }),
        );
      });

      it('passing IMember should create a new form with FormGroup', () => {
        const formGroup = service.createMemberFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            photo: expect.any(Object),
            email: expect.any(Object),
            phoneNumber: expect.any(Object),
            dateOfBirth: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            state: expect.any(Object),
            zipCode: expect.any(Object),
            cityOfBirth: expect.any(Object),
            previousReligion: expect.any(Object),
            maritalStatus: expect.any(Object),
            spouseName: expect.any(Object),
            dateOfMarriage: expect.any(Object),
            status: expect.any(Object),
            cpf: expect.any(Object),
            rg: expect.any(Object),
            dateOfBaptism: expect.any(Object),
            churchOfBaptism: expect.any(Object),
            dateOfMembership: expect.any(Object),
            typeOfMembership: expect.any(Object),
            associationMeetingMinutes: expect.any(Object),
            dateOfDeath: expect.any(Object),
            dateOfExit: expect.any(Object),
            exitDestination: expect.any(Object),
            exitReason: expect.any(Object),
            exitMeetingMinutes: expect.any(Object),
            notes: expect.any(Object),
            church: expect.any(Object),
            playIns: expect.any(Object),
            participateIns: expect.any(Object),
            memberOfs: expect.any(Object),
          }),
        );
      });
    });

    describe('getMember', () => {
      it('should return NewMember for default Member initial value', () => {
        const formGroup = service.createMemberFormGroup(sampleWithNewData);

        const member = service.getMember(formGroup) as any;

        expect(member).toMatchObject(sampleWithNewData);
      });

      it('should return NewMember for empty Member initial value', () => {
        const formGroup = service.createMemberFormGroup();

        const member = service.getMember(formGroup) as any;

        expect(member).toMatchObject({});
      });

      it('should return IMember', () => {
        const formGroup = service.createMemberFormGroup(sampleWithRequiredData);

        const member = service.getMember(formGroup) as any;

        expect(member).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMember should not enable id FormControl', () => {
        const formGroup = service.createMemberFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMember should disable id FormControl', () => {
        const formGroup = service.createMemberFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
