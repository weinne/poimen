import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { RoleSchedule } from 'app/entities/enumerations/role-schedule.model';
import { ScheduleService } from '../service/schedule.service';
import { ISchedule } from '../schedule.model';
import { ScheduleFormGroup, ScheduleFormService } from './schedule-form.service';

@Component({
  standalone: true,
  selector: 'jhi-schedule-update',
  templateUrl: './schedule-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ScheduleUpdateComponent implements OnInit {
  isSaving = false;
  schedule: ISchedule | null = null;
  roleScheduleValues = Object.keys(RoleSchedule);

  membersSharedCollection: IMember[] = [];
  worshipEventsSharedCollection: IWorshipEvent[] = [];

  protected scheduleService = inject(ScheduleService);
  protected scheduleFormService = inject(ScheduleFormService);
  protected memberService = inject(MemberService);
  protected worshipEventService = inject(WorshipEventService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ScheduleFormGroup = this.scheduleFormService.createScheduleFormGroup();

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  compareWorshipEvent = (o1: IWorshipEvent | null, o2: IWorshipEvent | null): boolean =>
    this.worshipEventService.compareWorshipEvent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schedule }) => {
      this.schedule = schedule;
      if (schedule) {
        this.updateForm(schedule);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schedule = this.scheduleFormService.getSchedule(this.editForm);
    if (schedule.id !== null) {
      this.subscribeToSaveResponse(this.scheduleService.update(schedule));
    } else {
      this.subscribeToSaveResponse(this.scheduleService.create(schedule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchedule>>): void {
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

  protected updateForm(schedule: ISchedule): void {
    this.schedule = schedule;
    this.scheduleFormService.resetForm(this.editForm, schedule);

    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      ...(schedule.members ?? []),
    );
    this.worshipEventsSharedCollection = this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
      this.worshipEventsSharedCollection,
      ...(schedule.worshipEvents ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(
        map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, ...(this.schedule?.members ?? []))),
      )
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.worshipEventService
      .query()
      .pipe(map((res: HttpResponse<IWorshipEvent[]>) => res.body ?? []))
      .pipe(
        map((worshipEvents: IWorshipEvent[]) =>
          this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
            worshipEvents,
            ...(this.schedule?.worshipEvents ?? []),
          ),
        ),
      )
      .subscribe((worshipEvents: IWorshipEvent[]) => (this.worshipEventsSharedCollection = worshipEvents));
  }
}
