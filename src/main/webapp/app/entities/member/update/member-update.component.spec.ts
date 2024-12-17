import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { ISchedule } from 'app/entities/schedule/schedule.model';
import { ScheduleService } from 'app/entities/schedule/service/schedule.service';
import { IMember } from '../member.model';
import { MemberService } from '../service/member.service';
import { MemberFormService } from './member-form.service';

import { MemberUpdateComponent } from './member-update.component';

describe('Member Management Update Component', () => {
  let comp: MemberUpdateComponent;
  let fixture: ComponentFixture<MemberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let memberFormService: MemberFormService;
  let memberService: MemberService;
  let churchService: ChurchService;
  let scheduleService: ScheduleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MemberUpdateComponent],
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
      .overrideTemplate(MemberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MemberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    memberFormService = TestBed.inject(MemberFormService);
    memberService = TestBed.inject(MemberService);
    churchService = TestBed.inject(ChurchService);
    scheduleService = TestBed.inject(ScheduleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const member: IMember = { id: 456 };
      const church: IChurch = { id: 4442 };
      member.church = church;

      const churchCollection: IChurch[] = [{ id: 878 }];
      jest.spyOn(churchService, 'query').mockReturnValue(of(new HttpResponse({ body: churchCollection })));
      const additionalChurches = [church];
      const expectedCollection: IChurch[] = [...additionalChurches, ...churchCollection];
      jest.spyOn(churchService, 'addChurchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(churchService.query).toHaveBeenCalled();
      expect(churchService.addChurchToCollectionIfMissing).toHaveBeenCalledWith(
        churchCollection,
        ...additionalChurches.map(expect.objectContaining),
      );
      expect(comp.churchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Schedule query and add missing value', () => {
      const member: IMember = { id: 456 };
      const schedules: ISchedule[] = [{ id: 32403 }];
      member.schedules = schedules;

      const scheduleCollection: ISchedule[] = [{ id: 3973 }];
      jest.spyOn(scheduleService, 'query').mockReturnValue(of(new HttpResponse({ body: scheduleCollection })));
      const additionalSchedules = [...schedules];
      const expectedCollection: ISchedule[] = [...additionalSchedules, ...scheduleCollection];
      jest.spyOn(scheduleService, 'addScheduleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(scheduleService.query).toHaveBeenCalled();
      expect(scheduleService.addScheduleToCollectionIfMissing).toHaveBeenCalledWith(
        scheduleCollection,
        ...additionalSchedules.map(expect.objectContaining),
      );
      expect(comp.schedulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const member: IMember = { id: 456 };
      const church: IChurch = { id: 27000 };
      member.church = church;
      const schedule: ISchedule = { id: 10305 };
      member.schedules = [schedule];

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.schedulesSharedCollection).toContain(schedule);
      expect(comp.member).toEqual(member);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMember>>();
      const member = { id: 123 };
      jest.spyOn(memberFormService, 'getMember').mockReturnValue(member);
      jest.spyOn(memberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ member });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: member }));
      saveSubject.complete();

      // THEN
      expect(memberFormService.getMember).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(memberService.update).toHaveBeenCalledWith(expect.objectContaining(member));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMember>>();
      const member = { id: 123 };
      jest.spyOn(memberFormService, 'getMember').mockReturnValue({ id: null });
      jest.spyOn(memberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ member: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: member }));
      saveSubject.complete();

      // THEN
      expect(memberFormService.getMember).toHaveBeenCalled();
      expect(memberService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMember>>();
      const member = { id: 123 };
      jest.spyOn(memberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ member });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(memberService.update).toHaveBeenCalled();
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

    describe('compareSchedule', () => {
      it('Should forward to scheduleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(scheduleService, 'compareSchedule');
        comp.compareSchedule(entity, entity2);
        expect(scheduleService.compareSchedule).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
