import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { MinistryGroupService } from 'app/entities/ministry-group/service/ministry-group.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IMinistryMembership } from '../ministry-membership.model';
import { MinistryMembershipService } from '../service/ministry-membership.service';
import { MinistryMembershipFormService } from './ministry-membership-form.service';

import { MinistryMembershipUpdateComponent } from './ministry-membership-update.component';

describe('MinistryMembership Management Update Component', () => {
  let comp: MinistryMembershipUpdateComponent;
  let fixture: ComponentFixture<MinistryMembershipUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ministryMembershipFormService: MinistryMembershipFormService;
  let ministryMembershipService: MinistryMembershipService;
  let ministryGroupService: MinistryGroupService;
  let memberService: MemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MinistryMembershipUpdateComponent],
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
      .overrideTemplate(MinistryMembershipUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MinistryMembershipUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ministryMembershipFormService = TestBed.inject(MinistryMembershipFormService);
    ministryMembershipService = TestBed.inject(MinistryMembershipService);
    ministryGroupService = TestBed.inject(MinistryGroupService);
    memberService = TestBed.inject(MemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MinistryGroup query and add missing value', () => {
      const ministryMembership: IMinistryMembership = { id: 456 };
      const ministryGroup: IMinistryGroup = { id: 15879 };
      ministryMembership.ministryGroup = ministryGroup;

      const ministryGroupCollection: IMinistryGroup[] = [{ id: 12338 }];
      jest.spyOn(ministryGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: ministryGroupCollection })));
      const additionalMinistryGroups = [ministryGroup];
      const expectedCollection: IMinistryGroup[] = [...additionalMinistryGroups, ...ministryGroupCollection];
      jest.spyOn(ministryGroupService, 'addMinistryGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ministryMembership });
      comp.ngOnInit();

      expect(ministryGroupService.query).toHaveBeenCalled();
      expect(ministryGroupService.addMinistryGroupToCollectionIfMissing).toHaveBeenCalledWith(
        ministryGroupCollection,
        ...additionalMinistryGroups.map(expect.objectContaining),
      );
      expect(comp.ministryGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Member query and add missing value', () => {
      const ministryMembership: IMinistryMembership = { id: 456 };
      const member: IMember = { id: 13526 };
      ministryMembership.member = member;

      const memberCollection: IMember[] = [{ id: 11803 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [member];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ministryMembership });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining),
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ministryMembership: IMinistryMembership = { id: 456 };
      const ministryGroup: IMinistryGroup = { id: 11116 };
      ministryMembership.ministryGroup = ministryGroup;
      const member: IMember = { id: 21598 };
      ministryMembership.member = member;

      activatedRoute.data = of({ ministryMembership });
      comp.ngOnInit();

      expect(comp.ministryGroupsSharedCollection).toContain(ministryGroup);
      expect(comp.membersSharedCollection).toContain(member);
      expect(comp.ministryMembership).toEqual(ministryMembership);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistryMembership>>();
      const ministryMembership = { id: 123 };
      jest.spyOn(ministryMembershipFormService, 'getMinistryMembership').mockReturnValue(ministryMembership);
      jest.spyOn(ministryMembershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministryMembership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ministryMembership }));
      saveSubject.complete();

      // THEN
      expect(ministryMembershipFormService.getMinistryMembership).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ministryMembershipService.update).toHaveBeenCalledWith(expect.objectContaining(ministryMembership));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistryMembership>>();
      const ministryMembership = { id: 123 };
      jest.spyOn(ministryMembershipFormService, 'getMinistryMembership').mockReturnValue({ id: null });
      jest.spyOn(ministryMembershipService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministryMembership: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ministryMembership }));
      saveSubject.complete();

      // THEN
      expect(ministryMembershipFormService.getMinistryMembership).toHaveBeenCalled();
      expect(ministryMembershipService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistryMembership>>();
      const ministryMembership = { id: 123 };
      jest.spyOn(ministryMembershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministryMembership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ministryMembershipService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMinistryGroup', () => {
      it('Should forward to ministryGroupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ministryGroupService, 'compareMinistryGroup');
        comp.compareMinistryGroup(entity, entity2);
        expect(ministryGroupService.compareMinistryGroup).toHaveBeenCalledWith(entity, entity2);
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
  });
});
