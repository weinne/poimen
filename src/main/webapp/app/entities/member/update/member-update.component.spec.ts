import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { MinistryGroupService } from 'app/entities/ministry-group/service/ministry-group.service';
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
  let worshipEventService: WorshipEventService;
  let ministryGroupService: MinistryGroupService;

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
    worshipEventService = TestBed.inject(WorshipEventService);
    ministryGroupService = TestBed.inject(MinistryGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const member: IMember = { id: 456 };
      const church: IChurch = { id: 26069 };
      member.church = church;

      const churchCollection: IChurch[] = [{ id: 25202 }];
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

    it('Should call WorshipEvent query and add missing value', () => {
      const member: IMember = { id: 456 };
      const playIns: IWorshipEvent[] = [{ id: 17213 }];
      member.playIns = playIns;
      const participateIns: IWorshipEvent[] = [{ id: 11809 }];
      member.participateIns = participateIns;

      const worshipEventCollection: IWorshipEvent[] = [{ id: 4259 }];
      jest.spyOn(worshipEventService, 'query').mockReturnValue(of(new HttpResponse({ body: worshipEventCollection })));
      const additionalWorshipEvents = [...playIns, ...participateIns];
      const expectedCollection: IWorshipEvent[] = [...additionalWorshipEvents, ...worshipEventCollection];
      jest.spyOn(worshipEventService, 'addWorshipEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(worshipEventService.query).toHaveBeenCalled();
      expect(worshipEventService.addWorshipEventToCollectionIfMissing).toHaveBeenCalledWith(
        worshipEventCollection,
        ...additionalWorshipEvents.map(expect.objectContaining),
      );
      expect(comp.worshipEventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MinistryGroup query and add missing value', () => {
      const member: IMember = { id: 456 };
      const memberOfs: IMinistryGroup[] = [{ id: 21489 }];
      member.memberOfs = memberOfs;

      const ministryGroupCollection: IMinistryGroup[] = [{ id: 3537 }];
      jest.spyOn(ministryGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: ministryGroupCollection })));
      const additionalMinistryGroups = [...memberOfs];
      const expectedCollection: IMinistryGroup[] = [...additionalMinistryGroups, ...ministryGroupCollection];
      jest.spyOn(ministryGroupService, 'addMinistryGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(ministryGroupService.query).toHaveBeenCalled();
      expect(ministryGroupService.addMinistryGroupToCollectionIfMissing).toHaveBeenCalledWith(
        ministryGroupCollection,
        ...additionalMinistryGroups.map(expect.objectContaining),
      );
      expect(comp.ministryGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const member: IMember = { id: 456 };
      const church: IChurch = { id: 4585 };
      member.church = church;
      const playIn: IWorshipEvent = { id: 20916 };
      member.playIns = [playIn];
      const participateIn: IWorshipEvent = { id: 24427 };
      member.participateIns = [participateIn];
      const memberOf: IMinistryGroup = { id: 899 };
      member.memberOfs = [memberOf];

      activatedRoute.data = of({ member });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.worshipEventsSharedCollection).toContain(playIn);
      expect(comp.worshipEventsSharedCollection).toContain(participateIn);
      expect(comp.ministryGroupsSharedCollection).toContain(memberOf);
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
  });
});
