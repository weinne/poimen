import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlan } from 'app/entities/plan/plan.model';
import { PlanService } from 'app/entities/plan/service/plan.service';
import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
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

  plansSharedCollection: IPlan[] = [];
  churchesSharedCollection: IChurch[] = [];
  usersSharedCollection: IUser[] = [];

  protected planSubscriptionService = inject(PlanSubscriptionService);
  protected planSubscriptionFormService = inject(PlanSubscriptionFormService);
  protected planService = inject(PlanService);
  protected churchService = inject(ChurchService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlanSubscriptionFormGroup = this.planSubscriptionFormService.createPlanSubscriptionFormGroup();

  comparePlan = (o1: IPlan | null, o2: IPlan | null): boolean => this.planService.comparePlan(o1, o2);

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

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

    this.plansSharedCollection = this.planService.addPlanToCollectionIfMissing<IPlan>(this.plansSharedCollection, planSubscription.plan);
    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      planSubscription.church,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, planSubscription.user);
  }

  protected loadRelationshipsOptions(): void {
    this.planService
      .query()
      .pipe(map((res: HttpResponse<IPlan[]>) => res.body ?? []))
      .pipe(map((plans: IPlan[]) => this.planService.addPlanToCollectionIfMissing<IPlan>(plans, this.planSubscription?.plan)))
      .subscribe((plans: IPlan[]) => (this.plansSharedCollection = plans));

    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(
        map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.planSubscription?.church)),
      )
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.planSubscription?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
