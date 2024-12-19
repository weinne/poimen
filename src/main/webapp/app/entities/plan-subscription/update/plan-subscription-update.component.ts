import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IPlan } from 'app/entities/plan/plan.model';
import { PlanService } from 'app/entities/plan/service/plan.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { PlanSubscriptionStatus } from 'app/entities/enumerations/plan-subscription-status.model';
import { PaymentProvider } from 'app/entities/enumerations/payment-provider.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';
import { PlanSubscriptionService } from '../service/plan-subscription.service';
import { IPlanSubscription } from '../plan-subscription.model';
import { PlanSubscriptionFormGroup, PlanSubscriptionFormService } from './plan-subscription-form.service';

@Component({
  standalone: true,
  selector: 'jhi-plan-subscription-update',
  templateUrl: './plan-subscription-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlanSubscriptionUpdateComponent implements OnInit {
  isSaving = false;
  planSubscription: IPlanSubscription | null = null;
  planSubscriptionStatusValues = Object.keys(PlanSubscriptionStatus);
  paymentProviderValues = Object.keys(PaymentProvider);
  paymentStatusValues = Object.keys(PaymentStatus);

  churchesSharedCollection: IChurch[] = [];
  plansSharedCollection: IPlan[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  protected planSubscriptionService = inject(PlanSubscriptionService);
  protected planSubscriptionFormService = inject(PlanSubscriptionFormService);
  protected churchService = inject(ChurchService);
  protected planService = inject(PlanService);
  protected applicationUserService = inject(ApplicationUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlanSubscriptionFormGroup = this.planSubscriptionFormService.createPlanSubscriptionFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  comparePlan = (o1: IPlan | null, o2: IPlan | null): boolean => this.planService.comparePlan(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planSubscription }) => {
      this.planSubscription = planSubscription;
      if (planSubscription) {
        this.updateForm(planSubscription);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const planSubscription = this.planSubscriptionFormService.getPlanSubscription(this.editForm);
    if (planSubscription.id !== null) {
      this.subscribeToSaveResponse(this.planSubscriptionService.update(planSubscription));
    } else {
      this.subscribeToSaveResponse(this.planSubscriptionService.create(planSubscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanSubscription>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(planSubscription: IPlanSubscription): void {
    this.planSubscription = planSubscription;
    this.planSubscriptionFormService.resetForm(this.editForm, planSubscription);

    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      planSubscription.church,
    );
    this.plansSharedCollection = this.planService.addPlanToCollectionIfMissing<IPlan>(this.plansSharedCollection, planSubscription.plan);
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      planSubscription.user,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(
        map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.planSubscription?.church)),
      )
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.planService
      .query()
      .pipe(map((res: HttpResponse<IPlan[]>) => res.body ?? []))
      .pipe(map((plans: IPlan[]) => this.planService.addPlanToCollectionIfMissing<IPlan>(plans, this.planSubscription?.plan)))
      .subscribe((plans: IPlan[]) => (this.plansSharedCollection = plans));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.planSubscription?.user,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }
}
