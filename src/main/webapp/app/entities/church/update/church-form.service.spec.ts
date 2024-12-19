import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../church.test-samples';

import { ChurchFormService } from './church-form.service';

describe('Church Form Service', () => {
  let service: ChurchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChurchFormService);
  });

  describe('Service methods', () => {
    describe('createChurchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChurchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cnpj: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            dateFoundation: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            website: expect.any(Object),
            facebook: expect.any(Object),
            instagram: expect.any(Object),
            twitter: expect.any(Object),
            youtube: expect.any(Object),
            about: expect.any(Object),
          }),
        );
      });

      it('passing IChurch should create a new form with FormGroup', () => {
        const formGroup = service.createChurchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            cnpj: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
            dateFoundation: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            website: expect.any(Object),
            facebook: expect.any(Object),
            instagram: expect.any(Object),
            twitter: expect.any(Object),
            youtube: expect.any(Object),
            about: expect.any(Object),
          }),
        );
      });
    });

    describe('getChurch', () => {
      it('should return NewChurch for default Church initial value', () => {
        const formGroup = service.createChurchFormGroup(sampleWithNewData);

        const church = service.getChurch(formGroup) as any;

        expect(church).toMatchObject(sampleWithNewData);
      });

      it('should return NewChurch for empty Church initial value', () => {
        const formGroup = service.createChurchFormGroup();

        const church = service.getChurch(formGroup) as any;

        expect(church).toMatchObject({});
      });

      it('should return IChurch', () => {
        const formGroup = service.createChurchFormGroup(sampleWithRequiredData);

        const church = service.getChurch(formGroup) as any;

        expect(church).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChurch should not enable id FormControl', () => {
        const formGroup = service.createChurchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChurch should disable id FormControl', () => {
        const formGroup = service.createChurchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
