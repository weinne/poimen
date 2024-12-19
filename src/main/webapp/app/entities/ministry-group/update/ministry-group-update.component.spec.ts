import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IMinistryGroup } from '../ministry-group.model';
import { MinistryGroupService } from '../service/ministry-group.service';
import { MinistryGroupFormService } from './ministry-group-form.service';

import { MinistryGroupUpdateComponent } from './ministry-group-update.component';

describe('MinistryGroup Management Update Component', () => {
  let comp: MinistryGroupUpdateComponent;
  let fixture: ComponentFixture<MinistryGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ministryGroupFormService: MinistryGroupFormService;
  let ministryGroupService: MinistryGroupService;
  let churchService: ChurchService;
  let memberService: MemberService;

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
    memberService = TestBed.inject(MemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const ministryGroup: IMinistryGroup = { id: 456 };
      const church: IChurch = { id: 12146 };
      ministryGroup.church = church;

      const churchCollection: IChurch[] = [{ id: 17877 }];
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

    it('Should call Member query and add missing value', () => {
      const ministryGroup: IMinistryGroup = { id: 456 };
      const president: IMember = { id: 31515 };
      ministryGroup.president = president;
      const supervisor: IMember = { id: 85 };
      ministryGroup.supervisor = supervisor;
      const members: IMember[] = [{ id: 14030 }];
      ministryGroup.members = members;

      const memberCollection: IMember[] = [{ id: 9109 }];
      jest.spyOn(memberService, 'query').mockReturnValue(of(new HttpResponse({ body: memberCollection })));
      const additionalMembers = [president, supervisor, ...members];
      const expectedCollection: IMember[] = [...additionalMembers, ...memberCollection];
      jest.spyOn(memberService, 'addMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ministryGroup });
      comp.ngOnInit();

      expect(memberService.query).toHaveBeenCalled();
      expect(memberService.addMemberToCollectionIfMissing).toHaveBeenCalledWith(
        memberCollection,
        ...additionalMembers.map(expect.objectContaining),
      );
      expect(comp.membersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ministryGroup: IMinistryGroup = { id: 456 };
      const church: IChurch = { id: 27849 };
      ministryGroup.church = church;
      const president: IMember = { id: 11591 };
      ministryGroup.president = president;
      const supervisor: IMember = { id: 3082 };
      ministryGroup.supervisor = supervisor;
      const members: IMember = { id: 6143 };
      ministryGroup.members = [members];

      activatedRoute.data = of({ ministryGroup });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.membersSharedCollection).toContain(president);
      expect(comp.membersSharedCollection).toContain(supervisor);
      expect(comp.membersSharedCollection).toContain(members);
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
