import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { MinistryGroupService } from '../service/ministry-group.service';
import { IMinistryGroup } from '../ministry-group.model';
import { MinistryGroupFormService } from './ministry-group-form.service';

import { MinistryGroupUpdateComponent } from './ministry-group-update.component';

describe('MinistryGroup Management Update Component', () => {
  let comp: MinistryGroupUpdateComponent;
  let fixture: ComponentFixture<MinistryGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ministryGroupFormService: MinistryGroupFormService;
  let ministryGroupService: MinistryGroupService;
  let churchService: ChurchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MinistryGroupUpdateComponent],
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
      .overrideTemplate(MinistryGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MinistryGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ministryGroupFormService = TestBed.inject(MinistryGroupFormService);
    ministryGroupService = TestBed.inject(MinistryGroupService);
    churchService = TestBed.inject(ChurchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const ministryGroup: IMinistryGroup = { id: 456 };
      const church: IChurch = { id: 2491 };
      ministryGroup.church = church;

      const churchCollection: IChurch[] = [{ id: 9849 }];
      jest.spyOn(churchService, 'query').mockReturnValue(of(new HttpResponse({ body: churchCollection })));
      const additionalChurches = [church];
      const expectedCollection: IChurch[] = [...additionalChurches, ...churchCollection];
      jest.spyOn(churchService, 'addChurchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ministryGroup });
      comp.ngOnInit();

      expect(churchService.query).toHaveBeenCalled();
      expect(churchService.addChurchToCollectionIfMissing).toHaveBeenCalledWith(
        churchCollection,
        ...additionalChurches.map(expect.objectContaining),
      );
      expect(comp.churchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ministryGroup: IMinistryGroup = { id: 456 };
      const church: IChurch = { id: 9173 };
      ministryGroup.church = church;

      activatedRoute.data = of({ ministryGroup });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.ministryGroup).toEqual(ministryGroup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistryGroup>>();
      const ministryGroup = { id: 123 };
      jest.spyOn(ministryGroupFormService, 'getMinistryGroup').mockReturnValue(ministryGroup);
      jest.spyOn(ministryGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministryGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ministryGroup }));
      saveSubject.complete();

      // THEN
      expect(ministryGroupFormService.getMinistryGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ministryGroupService.update).toHaveBeenCalledWith(expect.objectContaining(ministryGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistryGroup>>();
      const ministryGroup = { id: 123 };
      jest.spyOn(ministryGroupFormService, 'getMinistryGroup').mockReturnValue({ id: null });
      jest.spyOn(ministryGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministryGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ministryGroup }));
      saveSubject.complete();

      // THEN
      expect(ministryGroupFormService.getMinistryGroup).toHaveBeenCalled();
      expect(ministryGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistryGroup>>();
      const ministryGroup = { id: 123 };
      jest.spyOn(ministryGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministryGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ministryGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareChurch', () => {
      it('Should forward to churchService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(churchService, 'compareChurch');
        comp.compareChurch(entity, entity2);
        expect(churchService.compareChurch).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
