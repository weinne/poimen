import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IPlan } from 'app/entities/plan/plan.model';
import { PlanService } from 'app/entities/plan/service/plan.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
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
  let churchService: ChurchService;
  let planService: PlanService;
  let applicationUserService: ApplicationUserService;

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
    churchService = TestBed.inject(ChurchService);
    planService = TestBed.inject(PlanService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Church query and add missing value', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const church: IChurch = { id: 31606 };
      planSubscription.church = church;

      const churchCollection: IChurch[] = [{ id: 8971 }];
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

    it('Should call Plan query and add missing value', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const plan: IPlan = { id: 28575 };
      planSubscription.plan = plan;

      const planCollection: IPlan[] = [{ id: 17780 }];
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

    it('Should call ApplicationUser query and add missing value', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const user: IApplicationUser = { id: 9537 };
      planSubscription.user = user;

      const applicationUserCollection: IApplicationUser[] = [{ id: 29660 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [user];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const planSubscription: IPlanSubscription = { id: 456 };
      const church: IChurch = { id: 25945 };
      planSubscription.church = church;
      const plan: IPlan = { id: 11087 };
      planSubscription.plan = plan;
      const user: IApplicationUser = { id: 10483 };
      planSubscription.user = user;

      activatedRoute.data = of({ planSubscription });
      comp.ngOnInit();

      expect(comp.churchesSharedCollection).toContain(church);
      expect(comp.plansSharedCollection).toContain(plan);
      expect(comp.applicationUsersSharedCollection).toContain(user);
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
    describe('compareChurch', () => {
      it('Should forward to churchService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(churchService, 'compareChurch');
        comp.compareChurch(entity, entity2);
        expect(churchService.compareChurch).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePlan', () => {
      it('Should forward to planService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(planService, 'comparePlan');
        comp.comparePlan(entity, entity2);
        expect(planService.comparePlan).toHaveBeenCalledWith(entity, entity2);
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
