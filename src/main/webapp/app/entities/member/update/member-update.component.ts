import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { ISchedule } from 'app/entities/schedule/schedule.model';
import { ScheduleService } from 'app/entities/schedule/service/schedule.service';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { MemberService } from '../service/member.service';
import { IMember } from '../member.model';
import { MemberFormGroup, MemberFormService } from './member-form.service';

@Component({
  standalone: true,
  selector: 'jhi-member-update',
  templateUrl: './member-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MemberUpdateComponent implements OnInit {
  isSaving = false;
  member: IMember | null = null;

  churchesSharedCollection: IChurch[] = [];
  schedulesSharedCollection: ISchedule[] = [];
  worshipEventsSharedCollection: IWorshipEvent[] = [];

  protected memberService = inject(MemberService);
  protected memberFormService = inject(MemberFormService);
  protected churchService = inject(ChurchService);
  protected scheduleService = inject(ScheduleService);
  protected worshipEventService = inject(WorshipEventService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MemberFormGroup = this.memberFormService.createMemberFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareSchedule = (o1: ISchedule | null, o2: ISchedule | null): boolean => this.scheduleService.compareSchedule(o1, o2);

  compareWorshipEvent = (o1: IWorshipEvent | null, o2: IWorshipEvent | null): boolean =>
    this.worshipEventService.compareWorshipEvent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;
      if (member) {
        this.updateForm(member);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const member = this.memberFormService.getMember(this.editForm);
    if (member.id !== null) {
      this.subscribeToSaveResponse(this.memberService.update(member));
    } else {
      this.subscribeToSaveResponse(this.memberService.create(member));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>): void {
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

  protected updateForm(member: IMember): void {
    this.member = member;
    this.memberFormService.resetForm(this.editForm, member);

    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      member.church,
    );
    this.schedulesSharedCollection = this.scheduleService.addScheduleToCollectionIfMissing<ISchedule>(
      this.schedulesSharedCollection,
      ...(member.schedules ?? []),
    );
    this.worshipEventsSharedCollection = this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
      this.worshipEventsSharedCollection,
      ...(member.worshipEvents ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.member?.church)))
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.scheduleService
      .query()
      .pipe(map((res: HttpResponse<ISchedule[]>) => res.body ?? []))
      .pipe(
        map((schedules: ISchedule[]) =>
          this.scheduleService.addScheduleToCollectionIfMissing<ISchedule>(schedules, ...(this.member?.schedules ?? [])),
        ),
      )
      .subscribe((schedules: ISchedule[]) => (this.schedulesSharedCollection = schedules));

    this.worshipEventService
      .query()
      .pipe(map((res: HttpResponse<IWorshipEvent[]>) => res.body ?? []))
      .pipe(
        map((worshipEvents: IWorshipEvent[]) =>
          this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
            worshipEvents,
            ...(this.member?.worshipEvents ?? []),
          ),
        ),
      )
      .subscribe((worshipEvents: IWorshipEvent[]) => (this.worshipEventsSharedCollection = worshipEvents));
  }
}
