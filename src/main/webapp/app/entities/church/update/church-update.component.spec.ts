import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ChurchService } from '../service/church.service';
import { IChurch } from '../church.model';
import { ChurchFormService } from './church-form.service';

import { ChurchUpdateComponent } from './church-update.component';

describe('Church Management Update Component', () => {
  let comp: ChurchUpdateComponent;
  let fixture: ComponentFixture<ChurchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let churchFormService: ChurchFormService;
  let churchService: ChurchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChurchUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ChurchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChurchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    churchFormService = TestBed.inject(ChurchFormService);
    churchService = TestBed.inject(ChurchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const church: IChurch = { id: 456 };

      activatedRoute.data = of({ church });
      comp.ngOnInit();

      expect(comp.church).toEqual(church);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurch>>();
      const church = { id: 123 };
      jest.spyOn(churchFormService, 'getChurch').mockReturnValue(church);
      jest.spyOn(churchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ church });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: church }));
      saveSubject.complete();

      // THEN
      expect(churchFormService.getChurch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(churchService.update).toHaveBeenCalledWith(expect.objectContaining(church));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurch>>();
      const church = { id: 123 };
      jest.spyOn(churchFormService, 'getChurch').mockReturnValue({ id: null });
      jest.spyOn(churchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ church: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: church }));
      saveSubject.complete();

      // THEN
      expect(churchFormService.getChurch).toHaveBeenCalled();
      expect(churchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChurch>>();
      const church = { id: 123 };
      jest.spyOn(churchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ church });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(churchService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
