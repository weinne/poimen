import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ICounselingSession } from '../counseling-session.model';
import { CounselingSessionService } from '../service/counseling-session.service';
import { CounselingSessionFormService } from './counseling-session-form.service';

import { CounselingSessionUpdateComponent } from './counseling-session-update.component';

describe('CounselingSession Management Update Component', () => {
  let comp: CounselingSessionUpdateComponent;
  let fixture: ComponentFixture<CounselingSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let counselingSessionFormService: CounselingSessionFormService;
  let counselingSessionService: CounselingSessionService;
  let churchService: ChurchService;
  let memberService: MemberService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CounselingSessionUpdateComponent],
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
      .overrideTemplate(CounselingSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CounselingSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    counselingSessionFormService = TestBed.inject(CounselingSessionFormService);
    counselingSessionService = TestBed.inject(CounselingSessionService);
    churchService = TestBed.inject(ChurchService);
    memberService = TestBed.inject(MemberService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const counselingSession: ICounselingSession = { id: 456 };
      const church: IChurch = { id: 18130 };
      counselingSession.church = church;

      const churchCollection: IChurch[] = [{ id: 28176 }];
      jest.spyOn(churchService, 'query').mockReturnValue(of(new HttpResponse({ body: churchCollection })));
      const additionalChurches = [church];
      const expectedCollection: IChurch[] = [...additionalChurches, ...churchCollection];
      jest.spyOn(churchService, 'addChurchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ counselingSession });
      comp.ngOnInit();

      expect(churchService.query).toHaveBeenCalled();
      expect(churchService.addChurchToCollectionIfMissing).toHaveBeenCalledWith(
        churchCollection,
        ...additionalChurches.map(expect.objectContaining),
      );
      expect(comp.churchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const counselingSession: ICounselingSession = { id: 456 };
      const member: IMember = { id: 31560 };
      counselingSession.member = member;

      const memberCollection: IMember[] = [{ id: 4845 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ counselingSession });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining),
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const counselingSession: ICounselingSession = { id: 456 };
      const user: IUser = { id: 23543 };
      counselingSession.user = user;

      const userCollection: IUser[] = [{ id: 148 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ counselingSession });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const counselingSession: ICounselingSession = { id: 456 };
      const church: IChurch = { id: 23703 };
      counselingSession.church = church;
      const member: IMember = { id: 13772 };
      counselingSession.member = member;
      const user: IUser = { id: 12961 };
      counselingSession.user = user;

      activatedRoute.data = of({ counselingSession });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.counselingSession).toEqual(counselingSession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounselingSession>>();
      const counselingSession = { id: 123 };
      jest.spyOn(counselingSessionFormService, 'getCounselingSession').mockReturnValue(counselingSession);
      jest.spyOn(counselingSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counselingSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counselingSession }));
      saveSubject.complete();

      // THEN
      expect(counselingSessionFormService.getCounselingSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(counselingSessionService.update).toHaveBeenCalledWith(expect.objectContaining(counselingSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounselingSession>>();
      const counselingSession = { id: 123 };
      jest.spyOn(counselingSessionFormService, 'getCounselingSession').mockReturnValue({ id: null });
      jest.spyOn(counselingSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counselingSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counselingSession }));
      saveSubject.complete();

      // THEN
      expect(counselingSessionFormService.getCounselingSession).toHaveBeenCalled();
      expect(counselingSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICounselingSession>>();
      const counselingSession = { id: 123 };
      jest.spyOn(counselingSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counselingSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(counselingSessionService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
