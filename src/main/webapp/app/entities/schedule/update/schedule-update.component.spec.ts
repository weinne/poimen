import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { ISchedule } from '../schedule.model';
import { ScheduleService } from '../service/schedule.service';
import { ScheduleFormService } from './schedule-form.service';

import { ScheduleUpdateComponent } from './schedule-update.component';

describe('Schedule Management Update Component', () => {
  let comp: ScheduleUpdateComponent;
  let fixture: ComponentFixture<ScheduleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let scheduleFormService: ScheduleFormService;
  let scheduleService: ScheduleService;
  let memberService: MemberService;
  let worshipEventService: WorshipEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ScheduleUpdateComponent],
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
      .overrideTemplate(ScheduleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ScheduleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scheduleFormService = TestBed.inject(ScheduleFormService);
    scheduleService = TestBed.inject(ScheduleService);
    memberService = TestBed.inject(MemberService);
    worshipEventService = TestBed.inject(WorshipEventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Member query and add missing value', () => {
      const schedule: ISchedule = { id: 456 };
      const members: IMember[] = [{ id: 2447 }];
      schedule.members = members;

      const memberCollection: IMember[] = [{ id: 26287 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [...members];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ schedule });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining),
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WorshipEvent query and add missing value', () => {
      const schedule: ISchedule = { id: 456 };
      const worshipEvents: IWorshipEvent[] = [{ id: 29652 }];
      schedule.worshipEvents = worshipEvents;

      const worshipEventCollection: IWorshipEvent[] = [{ id: 23678 }];
      jest.spyOn(worshipEventService, 'query').mockReturnValue(of(new HttpResponse({ body: worshipEventCollection })));
      const additionalWorshipEvents = [...worshipEvents];
      const expectedCollection: IWorshipEvent[] = [...additionalWorshipEvents, ...worshipEventCollection];
      jest.spyOn(worshipEventService, 'addWorshipEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ schedule });
      comp.ngOnInit();

      expect(worshipEventService.query).toHaveBeenCalled();
      expect(worshipEventService.addWorshipEventToCollectionIfMissing).toHaveBeenCalledWith(
        worshipEventCollection,
        ...additionalWorshipEvents.map(expect.objectContaining),
      );
      expect(comp.worshipEventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const schedule: ISchedule = { id: 456 };
      const member: IMember = { id: 26494 };
      schedule.members = [member];
      const worshipEvent: IWorshipEvent = { id: 18194 };
      schedule.worshipEvents = [worshipEvent];

      activatedRoute.data = of({ schedule });
      comp.ngOnInit();

      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.worshipEventsSharedCollection).toContain(worshipEvent);
      expect(comp.schedule).toEqual(schedule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchedule>>();
      const schedule = { id: 123 };
      jest.spyOn(scheduleFormService, 'getSchedule').mockReturnValue(schedule);
      jest.spyOn(scheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schedule }));
      saveSubject.complete();

      // THEN
      expect(scheduleFormService.getSchedule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scheduleService.update).toHaveBeenCalledWith(expect.objectContaining(schedule));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchedule>>();
      const schedule = { id: 123 };
      jest.spyOn(scheduleFormService, 'getSchedule').mockReturnValue({ id: null });
      jest.spyOn(scheduleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schedule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schedule }));
      saveSubject.complete();

      // THEN
      expect(scheduleFormService.getSchedule).toHaveBeenCalled();
      expect(scheduleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchedule>>();
      const schedule = { id: 123 };
      jest.spyOn(scheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scheduleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMember', () => {
      it('Should forward to memberService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(memberService, 'compareMember');
        comp.compareMember(entity, entity2);
        expect(memberService.compareMember).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
