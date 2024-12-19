import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { HymnService } from '../service/hymn.service';
import { IHymn } from '../hymn.model';
import { HymnFormService } from './hymn-form.service';

import { HymnUpdateComponent } from './hymn-update.component';

describe('Hymn Management Update Component', () => {
  let comp: HymnUpdateComponent;
  let fixture: ComponentFixture<HymnUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hymnFormService: HymnFormService;
  let hymnService: HymnService;
  let worshipEventService: WorshipEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HymnUpdateComponent],
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
      .overrideTemplate(HymnUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HymnUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hymnFormService = TestBed.inject(HymnFormService);
    hymnService = TestBed.inject(HymnService);
    worshipEventService = TestBed.inject(WorshipEventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call WorshipEvent query and add missing value', () => {
      const hymn: IHymn = { id: 456 };
      const services: IWorshipEvent[] = [{ id: 25097 }];
      hymn.services = services;

      const worshipEventCollection: IWorshipEvent[] = [{ id: 26806 }];
      jest.spyOn(worshipEventService, 'query').mockReturnValue(of(new HttpResponse({ body: worshipEventCollection })));
      const additionalWorshipEvents = [...services];
      const expectedCollection: IWorshipEvent[] = [...additionalWorshipEvents, ...worshipEventCollection];
      jest.spyOn(worshipEventService, 'addWorshipEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hymn });
      comp.ngOnInit();

      expect(worshipEventService.query).toHaveBeenCalled();
      expect(worshipEventService.addWorshipEventToCollectionIfMissing).toHaveBeenCalledWith(
        worshipEventCollection,
        ...additionalWorshipEvents.map(expect.objectContaining),
      );
      expect(comp.worshipEventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hymn: IHymn = { id: 456 };
      const services: IWorshipEvent = { id: 13962 };
      hymn.services = [services];

      activatedRoute.data = of({ hymn });
      comp.ngOnInit();

      expect(comp.worshipEventsSharedCollection).toContain(services);
      expect(comp.hymn).toEqual(hymn);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHymn>>();
      const hymn = { id: 123 };
      jest.spyOn(hymnFormService, 'getHymn').mockReturnValue(hymn);
      jest.spyOn(hymnService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hymn });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hymn }));
      saveSubject.complete();

      // THEN
      expect(hymnFormService.getHymn).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hymnService.update).toHaveBeenCalledWith(expect.objectContaining(hymn));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHymn>>();
      const hymn = { id: 123 };
      jest.spyOn(hymnFormService, 'getHymn').mockReturnValue({ id: null });
      jest.spyOn(hymnService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hymn: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hymn }));
      saveSubject.complete();

      // THEN
      expect(hymnFormService.getHymn).toHaveBeenCalled();
      expect(hymnService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHymn>>();
      const hymn = { id: 123 };
      jest.spyOn(hymnService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hymn });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hymnService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWorshipEvent', () => {
      it('Should forward to worshipEventService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(worshipEventService, 'compareWorshipEvent');
        comp.compareWorshipEvent(entity, entity2);
        expect(worshipEventService.compareWorshipEvent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
