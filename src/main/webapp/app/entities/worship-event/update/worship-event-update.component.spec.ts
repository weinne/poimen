import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IHymn } from 'app/entities/hymn/hymn.model';
import { HymnService } from 'app/entities/hymn/service/hymn.service';
import { IWorshipEvent } from '../worship-event.model';
import { WorshipEventService } from '../service/worship-event.service';
import { WorshipEventFormService } from './worship-event-form.service';

import { WorshipEventUpdateComponent } from './worship-event-update.component';

describe('WorshipEvent Management Update Component', () => {
  let comp: WorshipEventUpdateComponent;
  let fixture: ComponentFixture<WorshipEventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let worshipEventFormService: WorshipEventFormService;
  let worshipEventService: WorshipEventService;
  let churchService: ChurchService;
  let memberService: MemberService;
  let hymnService: HymnService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WorshipEventUpdateComponent],
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
      .overrideTemplate(WorshipEventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorshipEventUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    worshipEventFormService = TestBed.inject(WorshipEventFormService);
    worshipEventService = TestBed.inject(WorshipEventService);
    churchService = TestBed.inject(ChurchService);
    memberService = TestBed.inject(MemberService);
    hymnService = TestBed.inject(HymnService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const worshipEvent: IWorshipEvent = { id: 456 };
      const church: IChurch = { id: 805 };
      worshipEvent.church = church;

      const churchCollection: IChurch[] = [{ id: 17483 }];
      jest.spyOn(churchService, 'query').mockReturnValue(of(new HttpResponse({ body: churchCollection })));
      const additionalChurches = [church];
      const expectedCollection: IChurch[] = [...additionalChurches, ...churchCollection];
      jest.spyOn(churchService, 'addChurchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ worshipEvent });
      comp.ngOnInit();

      expect(churchService.query).toHaveBeenCalled();
      expect(churchService.addChurchToCollectionIfMissing).toHaveBeenCalledWith(
        churchCollection,
        ...additionalChurches.map(expect.objectContaining),
      );
      expect(comp.churchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const worshipEvent: IWorshipEvent = { id: 456 };
      const preacher: IMember = { id: 8548 };
      worshipEvent.preacher = preacher;
      const liturgist: IMember = { id: 2657 };
      worshipEvent.liturgist = liturgist;
      const musicians: IMember[] = [{ id: 5715 }];
      worshipEvent.musicians = musicians;
      const participants: IMember[] = [{ id: 8104 }];
      worshipEvent.participants = participants;

      const memberCollection: IMember[] = [{ id: 6115 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [preacher, liturgist, ...musicians, ...participants];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ worshipEvent });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining),
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Hymn query and add missing value', () => {
      const worshipEvent: IWorshipEvent = { id: 456 };
      const hymns: IHymn[] = [{ id: 16041 }];
      worshipEvent.hymns = hymns;

      const hymnCollection: IHymn[] = [{ id: 768 }];
      jest.spyOn(hymnService, 'query').mockReturnValue(of(new HttpResponse({ body: hymnCollection })));
      const additionalHymns = [...hymns];
      const expectedCollection: IHymn[] = [...additionalHymns, ...hymnCollection];
      jest.spyOn(hymnService, 'addHymnToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ worshipEvent });
      comp.ngOnInit();

      expect(hymnService.query).toHaveBeenCalled();
      expect(hymnService.addHymnToCollectionIfMissing).toHaveBeenCalledWith(
        hymnCollection,
        ...additionalHymns.map(expect.objectContaining),
      );
      expect(comp.hymnsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const worshipEvent: IWorshipEvent = { id: 456 };
      const church: IChurch = { id: 20995 };
      worshipEvent.church = church;
      const preacher: IMember = { id: 10545 };
      worshipEvent.preacher = preacher;
      const liturgist: IMember = { id: 21143 };
      worshipEvent.liturgist = liturgist;
      const musicians: IMember = { id: 6489 };
      worshipEvent.musicians = [musicians];
      const participants: IMember = { id: 19537 };
      worshipEvent.participants = [participants];
      const hymns: IHymn = { id: 3179 };
      worshipEvent.hymns = [hymns];

      activatedRoute.data = of({ worshipEvent });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.membersSharedCollection).toContain(preacher);
      expect(comp.membersSharedCollection).toContain(liturgist);
      expect(comp.membersSharedCollection).toContain(musicians);
      expect(comp.membersSharedCollection).toContain(participants);
      expect(comp.hymnsSharedCollection).toContain(hymns);
      expect(comp.worshipEvent).toEqual(worshipEvent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorshipEvent>>();
      const worshipEvent = { id: 123 };
      jest.spyOn(worshipEventFormService, 'getWorshipEvent').mockReturnValue(worshipEvent);
      jest.spyOn(worshipEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ worshipEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: worshipEvent }));
      saveSubject.complete();

      // THEN
      expect(worshipEventFormService.getWorshipEvent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(worshipEventService.update).toHaveBeenCalledWith(expect.objectContaining(worshipEvent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorshipEvent>>();
      const worshipEvent = { id: 123 };
      jest.spyOn(worshipEventFormService, 'getWorshipEvent').mockReturnValue({ id: null });
      jest.spyOn(worshipEventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ worshipEvent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: worshipEvent }));
      saveSubject.complete();

      // THEN
      expect(worshipEventFormService.getWorshipEvent).toHaveBeenCalled();
      expect(worshipEventService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorshipEvent>>();
      const worshipEvent = { id: 123 };
      jest.spyOn(worshipEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ worshipEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(worshipEventService.update).toHaveBeenCalled();
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

    describe('compareHymn', () => {
      it('Should forward to hymnService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(hymnService, 'compareHymn');
        comp.compareHymn(entity, entity2);
        expect(hymnService.compareHymn).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
