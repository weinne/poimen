import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ministry-group.test-samples';

import { MinistryGroupFormService } from './ministry-group-form.service';

describe('MinistryGroup Form Service', () => {
  let service: MinistryGroupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MinistryGroupFormService);
  });

  describe('Service methods', () => {
    describe('createMinistryGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMinistryGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            establishedDate: expect.any(Object),
            type: expect.any(Object),
            church: expect.any(Object),
            president: expect.any(Object),
            supervisor: expect.any(Object),
            members: expect.any(Object),
          }),
        );
      });

      it('passing IMinistryGroup should create a new form with FormGroup', () => {
        const formGroup = service.createMinistryGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            establishedDate: expect.any(Object),
            type: expect.any(Object),
            church: expect.any(Object),
            president: expect.any(Object),
            supervisor: expect.any(Object),
            members: expect.any(Object),
          }),
        );
      });
    });

    describe('getMinistryGroup', () => {
      it('should return NewMinistryGroup for default MinistryGroup initial value', () => {
        const formGroup = service.createMinistryGroupFormGroup(sampleWithNewData);

        const ministryGroup = service.getMinistryGroup(formGroup) as any;

        expect(ministryGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewMinistryGroup for empty MinistryGroup initial value', () => {
        const formGroup = service.createMinistryGroupFormGroup();

        const ministryGroup = service.getMinistryGroup(formGroup) as any;

        expect(ministryGroup).toMatchObject({});
      });

      it('should return IMinistryGroup', () => {
        const formGroup = service.createMinistryGroupFormGroup(sampleWithRequiredData);

        const ministryGroup = service.getMinistryGroup(formGroup) as any;

        expect(ministryGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMinistryGroup should not enable id FormControl', () => {
        const formGroup = service.createMinistryGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMinistryGroup should disable id FormControl', () => {
        const formGroup = service.createMinistryGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
