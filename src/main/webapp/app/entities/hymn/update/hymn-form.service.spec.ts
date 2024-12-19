import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../hymn.test-samples';

import { HymnFormService } from './hymn-form.service';

describe('Hymn Form Service', () => {
  let service: HymnFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HymnFormService);
  });

  describe('Service methods', () => {
    describe('createHymnFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHymnFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            lyricsAuthor: expect.any(Object),
            musicAuthor: expect.any(Object),
            hymnary: expect.any(Object),
            hymnNumber: expect.any(Object),
            link: expect.any(Object),
            youtubeLink: expect.any(Object),
            sheetMusic: expect.any(Object),
            midi: expect.any(Object),
            tone: expect.any(Object),
            lyrics: expect.any(Object),
            services: expect.any(Object),
          }),
        );
      });

      it('passing IHymn should create a new form with FormGroup', () => {
        const formGroup = service.createHymnFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            lyricsAuthor: expect.any(Object),
            musicAuthor: expect.any(Object),
            hymnary: expect.any(Object),
            hymnNumber: expect.any(Object),
            link: expect.any(Object),
            youtubeLink: expect.any(Object),
            sheetMusic: expect.any(Object),
            midi: expect.any(Object),
            tone: expect.any(Object),
            lyrics: expect.any(Object),
            services: expect.any(Object),
          }),
        );
      });
    });

    describe('getHymn', () => {
      it('should return NewHymn for default Hymn initial value', () => {
        const formGroup = service.createHymnFormGroup(sampleWithNewData);

        const hymn = service.getHymn(formGroup) as any;

        expect(hymn).toMatchObject(sampleWithNewData);
      });

      it('should return NewHymn for empty Hymn initial value', () => {
        const formGroup = service.createHymnFormGroup();

        const hymn = service.getHymn(formGroup) as any;

        expect(hymn).toMatchObject({});
      });

      it('should return IHymn', () => {
        const formGroup = service.createHymnFormGroup(sampleWithRequiredData);

        const hymn = service.getHymn(formGroup) as any;

        expect(hymn).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHymn should not enable id FormControl', () => {
        const formGroup = service.createHymnFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHymn should disable id FormControl', () => {
        const formGroup = service.createHymnFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
