import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { MinistryGroupService } from 'app/entities/ministry-group/service/ministry-group.service';
import { ICounselingSession } from 'app/entities/counseling-session/counseling-session.model';
import { CounselingSessionService } from 'app/entities/counseling-session/service/counseling-session.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IAppointment } from '../appointment.model';
import { AppointmentService } from '../service/appointment.service';
import { AppointmentFormService } from './appointment-form.service';

import { AppointmentUpdateComponent } from './appointment-update.component';

describe('Appointment Management Update Component', () => {
  let comp: AppointmentUpdateComponent;
  let fixture: ComponentFixture<AppointmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appointmentFormService: AppointmentFormService;
  let appointmentService: AppointmentService;
  let churchService: ChurchService;
  let memberService: MemberService;
  let worshipEventService: WorshipEventService;
  let ministryGroupService: MinistryGroupService;
  let counselingSessionService: CounselingSessionService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AppointmentUpdateComponent],
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
      .overrideTemplate(AppointmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppointmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appointmentFormService = TestBed.inject(AppointmentFormService);
    appointmentService = TestBed.inject(AppointmentService);
    churchService = TestBed.inject(ChurchService);
    memberService = TestBed.inject(MemberService);
    worshipEventService = TestBed.inject(WorshipEventService);
    ministryGroupService = TestBed.inject(MinistryGroupService);
    counselingSessionService = TestBed.inject(CounselingSessionService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const church: IChurch = { id: 27208 };
      appointment.church = church;

      const churchCollection: IChurch[] = [{ id: 6672 }];
      jest.spyOn(churchService, 'query').mockReturnValue(of(new HttpResponse({ body: churchCollection })));
      const additionalChurches = [church];
      const expectedCollection: IChurch[] = [...additionalChurches, ...churchCollection];
      jest.spyOn(churchService, 'addChurchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(churchService.query).toHaveBeenCalled();
      expect(churchService.addChurchToCollectionIfMissing).toHaveBeenCalledWith(
        churchCollection,
        ...additionalChurches.map(expect.objectContaining),
      );
      expect(comp.churchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const member: IMember = { id: 30590 };
      appointment.member = member;

      const memberCollection: IMember[] = [{ id: 1689 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining),
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WorshipEvent query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const service: IWorshipEvent = { id: 8299 };
      appointment.service = service;

      const worshipEventCollection: IWorshipEvent[] = [{ id: 18238 }];
      jest.spyOn(worshipEventService, 'query').mockReturnValue(of(new HttpResponse({ body: worshipEventCollection })));
      const additionalWorshipEvents = [service];
      const expectedCollection: IWorshipEvent[] = [...additionalWorshipEvents, ...worshipEventCollection];
      jest.spyOn(worshipEventService, 'addWorshipEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(worshipEventService.query).toHaveBeenCalled();
      expect(worshipEventService.addWorshipEventToCollectionIfMissing).toHaveBeenCalledWith(
        worshipEventCollection,
        ...additionalWorshipEvents.map(expect.objectContaining),
      );
      expect(comp.worshipEventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MinistryGroup query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const group: IMinistryGroup = { id: 20619 };
      appointment.group = group;

      const ministryGroupCollection: IMinistryGroup[] = [{ id: 4888 }];
      jest.spyOn(ministryGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: ministryGroupCollection })));
      const additionalMinistryGroups = [group];
      const expectedCollection: IMinistryGroup[] = [...additionalMinistryGroups, ...ministryGroupCollection];
      jest.spyOn(ministryGroupService, 'addMinistryGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(ministryGroupService.query).toHaveBeenCalled();
      expect(ministryGroupService.addMinistryGroupToCollectionIfMissing).toHaveBeenCalledWith(
        ministryGroupCollection,
        ...additionalMinistryGroups.map(expect.objectContaining),
      );
      expect(comp.ministryGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CounselingSession query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const counselingSession: ICounselingSession = { id: 6269 };
      appointment.counselingSession = counselingSession;

      const counselingSessionCollection: ICounselingSession[] = [{ id: 21345 }];
      jest.spyOn(counselingSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: counselingSessionCollection })));
      const additionalCounselingSessions = [counselingSession];
      const expectedCollection: ICounselingSession[] = [...additionalCounselingSessions, ...counselingSessionCollection];
      jest.spyOn(counselingSessionService, 'addCounselingSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(counselingSessionService.query).toHaveBeenCalled();
      expect(counselingSessionService.addCounselingSessionToCollectionIfMissing).toHaveBeenCalledWith(
        counselingSessionCollection,
        ...additionalCounselingSessions.map(expect.objectContaining),
      );
      expect(comp.counselingSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const user: IApplicationUser = { id: 13981 };
      appointment.user = user;

      const applicationUserCollection: IApplicationUser[] = [{ id: 18194 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [user];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appointment: IAppointment = { id: 456 };
      const church: IChurch = { id: 17885 };
      appointment.church = church;
      const member: IMember = { id: 18700 };
      appointment.member = member;
      const service: IWorshipEvent = { id: 16224 };
      appointment.service = service;
      const group: IMinistryGroup = { id: 2079 };
      appointment.group = group;
      const counselingSession: ICounselingSession = { id: 499 };
      appointment.counselingSession = counselingSession;
      const user: IApplicationUser = { id: 31932 };
      appointment.user = user;

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.worshipEventsSharedCollection).toContain(service);
      expect(comp.ministryGroupsSharedCollection).toContain(group);
      expect(comp.counselingSessionsSharedCollection).toContain(counselingSession);
      expect(comp.applicationUsersSharedCollection).toContain(user);
      expect(comp.appointment).toEqual(appointment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentFormService, 'getAppointment').mockReturnValue(appointment);
      jest.spyOn(appointmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointment }));
      saveSubject.complete();

      // THEN
      expect(appointmentFormService.getAppointment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appointmentService.update).toHaveBeenCalledWith(expect.objectContaining(appointment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentFormService, 'getAppointment').mockReturnValue({ id: null });
      jest.spyOn(appointmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointment }));
      saveSubject.complete();

      // THEN
      expect(appointmentFormService.getAppointment).toHaveBeenCalled();
      expect(appointmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appointmentService.update).toHaveBeenCalled();
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

    describe('compareMinistryGroup', () => {
      it('Should forward to ministryGroupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ministryGroupService, 'compareMinistryGroup');
        comp.compareMinistryGroup(entity, entity2);
        expect(ministryGroupService.compareMinistryGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCounselingSession', () => {
      it('Should forward to counselingSessionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(counselingSessionService, 'compareCounselingSession');
        comp.compareCounselingSession(entity, entity2);
        expect(counselingSessionService.compareCounselingSession).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareApplicationUser', () => {
      it('Should forward to applicationUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
