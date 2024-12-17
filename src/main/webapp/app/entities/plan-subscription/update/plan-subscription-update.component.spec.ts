import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPlan } from 'app/entities/plan/plan.model';
import { PlanService } from 'app/entities/plan/service/plan.service';
import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IPlanSubscription } from '../plan-subscription.model';
import { PlanSubscriptionService } from '../service/plan-subscription.service';
import { PlanSubscriptionFormService } from './plan-subscription-form.service';

import { PlanSubscriptionUpdateComponent } from './plan-subscription-update.component';

describe('PlanSubscription Management Update Component', () => {
  let comp: PlanSubscriptionUpdateComponent;
  let fixture: ComponentFixture<PlanSubscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let planSubscriptionFormService: PlanSubscriptionFormService;
  let planSubscriptionService: PlanSubscriptionService;
  let planService: PlanService;
  let churchService: ChurchService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlanSubscriptionUpdateComponent],
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
      .overrideTemplate(PlanSubscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlanSubscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    planSubscriptionFormService = TestBed.inject(PlanSubscriptionFormService);
    planSubscriptionService = TestBed.inject(PlanSubscriptionService);
    planService = TestBed.inject(PlanService);
    churchService = TestBed.inject(ChurchService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Plan query and add missing value', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const plan: IPlan = { id: 6605 };
      planSubscription.plan = plan;

      const planCollection: IPlan[] = [{ id: 27843 }];
      jest.spyOn(planService, 'query').mockReturnValue(of(new HttpResponse({ body: planCollection })));
      const additionalPlans = [plan];
      const expectedCollection: IPlan[] = [...additionalPlans, ...planCollection];
      jest.spyOn(planService, 'addPlanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      expect(planService.query).toHaveBeenCalled();
      expect(planService.addPlanToCollectionIfMissing).toHaveBeenCalledWith(
        planCollection,
        ...additionalPlans.map(expect.objectContaining),
      );
      expect(comp.plansSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Church query and add missing value', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const church: IChurch = { id: 24892 };
      planSubscription.church = church;

      const churchCollection: IChurch[] = [{ id: 116 }];
      jest.spyOn(churchService, 'query').mockReturnValue(of(new HttpResponse({ body: churchCollection })));
      const additionalChurches = [church];
      const expectedCollection: IChurch[] = [...additionalChurches, ...churchCollection];
      jest.spyOn(churchService, 'addChurchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      expect(churchService.query).toHaveBeenCalled();
      expect(churchService.addChurchToCollectionIfMissing).toHaveBeenCalledWith(
        churchCollection,
        ...additionalChurches.map(expect.objectContaining),
      );
      expect(comp.churchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const user: IUser = { id: 13981 };
      planSubscription.user = user;

      const userCollection: IUser[] = [{ id: 18194 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const plan: IPlan = { id: 9246 };
      planSubscription.plan = plan;
      const church: IChurch = { id: 17701 };
      planSubscription.church = church;
      const user: IUser = { id: 31932 };
      planSubscription.user = user;

      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      expect(comp.plansSharedCollection).toContain(plan);
      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.planSubscription).toEqual(planSubscription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlanSubscription>>();
      const planSubscription = { id: 123 };
      jest.spyOn(planSubscriptionFormService, 'getPlanSubscription').mockReturnValue(planSubscription);
      jest.spyOn(planSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planSubscription }));
      saveSubject.complete();

      // THEN
      expect(planSubscriptionFormService.getPlanSubscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(planSubscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(planSubscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlanSubscription>>();
      const planSubscription = { id: 123 };
      jest.spyOn(planSubscriptionFormService, 'getPlanSubscription').mockReturnValue({ id: null });
      jest.spyOn(planSubscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planSubscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planSubscription }));
      saveSubject.complete();

      // THEN
      expect(planSubscriptionFormService.getPlanSubscription).toHaveBeenCalled();
      expect(planSubscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlanSubscription>>();
      const planSubscription = { id: 123 };
      jest.spyOn(planSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(planSubscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlan', () => {
      it('Should forward to planService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(planService, 'comparePlan');
        comp.comparePlan(entity, entity2);
        expect(planService.comparePlan).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareChurch', () => {
      it('Should forward to churchService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(churchService, 'compareChurch');
        comp.compareChurch(entity, entity2);
        expect(churchService.compareChurch).toHaveBeenCalledWith(entity, entity2);
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
