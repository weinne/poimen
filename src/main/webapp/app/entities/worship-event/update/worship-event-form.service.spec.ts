import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../worship-event.test-samples';

import { WorshipEventFormService } from './worship-event-form.service';

describe('WorshipEvent Form Service', () => {
  let service: WorshipEventFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorshipEventFormService);
  });

  describe('Service methods', () => {
    describe('createWorshipEventFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorshipEventFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            title: expect.any(Object),
            guestPreacher: expect.any(Object),
            description: expect.any(Object),
            callToWorshipText: expect.any(Object),
            confessionOfSinText: expect.any(Object),
            assuranceOfPardonText: expect.any(Object),
            lordSupperText: expect.any(Object),
            benedictionText: expect.any(Object),
            confessionalText: expect.any(Object),
            sermonText: expect.any(Object),
            sermonFile: expect.any(Object),
            sermonLink: expect.any(Object),
            youtubeLink: expect.any(Object),
            bulletinFile: expect.any(Object),
            worshipType: expect.any(Object),
            church: expect.any(Object),
            preacher: expect.any(Object),
            liturgist: expect.any(Object),
            hymns: expect.any(Object),
            musicians: expect.any(Object),
            participants: expect.any(Object),
          }),
        );
      });

      it('passing IWorshipEvent should create a new form with FormGroup', () => {
        const formGroup = service.createWorshipEventFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            title: expect.any(Object),
            guestPreacher: expect.any(Object),
            description: expect.any(Object),
            callToWorshipText: expect.any(Object),
            confessionOfSinText: expect.any(Object),
            assuranceOfPardonText: expect.any(Object),
            lordSupperText: expect.any(Object),
            benedictionText: expect.any(Object),
            confessionalText: expect.any(Object),
            sermonText: expect.any(Object),
            sermonFile: expect.any(Object),
            sermonLink: expect.any(Object),
            youtubeLink: expect.any(Object),
            bulletinFile: expect.any(Object),
            worshipType: expect.any(Object),
            church: expect.any(Object),
            preacher: expect.any(Object),
            liturgist: expect.any(Object),
            hymns: expect.any(Object),
            musicians: expect.any(Object),
            participants: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorshipEvent', () => {
      it('should return NewWorshipEvent for default WorshipEvent initial value', () => {
        const formGroup = service.createWorshipEventFormGroup(sampleWithNewData);

        const worshipEvent = service.getWorshipEvent(formGroup) as any;

        expect(worshipEvent).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorshipEvent for empty WorshipEvent initial value', () => {
        const formGroup = service.createWorshipEventFormGroup();

        const worshipEvent = service.getWorshipEvent(formGroup) as any;

        expect(worshipEvent).toMatchObject({});
      });

      it('should return IWorshipEvent', () => {
        const formGroup = service.createWorshipEventFormGroup(sampleWithRequiredData);

        const worshipEvent = service.getWorshipEvent(formGroup) as any;

        expect(worshipEvent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorshipEvent should not enable id FormControl', () => {
        const formGroup = service.createWorshipEventFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorshipEvent should disable id FormControl', () => {
        const formGroup = service.createWorshipEventFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
