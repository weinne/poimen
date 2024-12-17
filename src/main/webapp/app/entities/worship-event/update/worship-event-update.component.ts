import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IHymn } from 'app/entities/hymn/hymn.model';
import { HymnService } from 'app/entities/hymn/service/hymn.service';
import { ISchedule } from 'app/entities/schedule/schedule.model';
import { ScheduleService } from 'app/entities/schedule/service/schedule.service';
import { WorshipType } from 'app/entities/enumerations/worship-type.model';
import { WorshipEventService } from '../service/worship-event.service';
import { IWorshipEvent } from '../worship-event.model';
import { WorshipEventFormGroup, WorshipEventFormService } from './worship-event-form.service';

@Component({
  standalone: true,
  selector: 'jhi-worship-event-update',
  templateUrl: './worship-event-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorshipEventUpdateComponent implements OnInit {
  isSaving = false;
  worshipEvent: IWorshipEvent | null = null;
  worshipTypeValues = Object.keys(WorshipType);

  churchesSharedCollection: IChurch[] = [];
  hymnsSharedCollection: IHymn[] = [];
  schedulesSharedCollection: ISchedule[] = [];

  protected worshipEventService = inject(WorshipEventService);
  protected worshipEventFormService = inject(WorshipEventFormService);
  protected churchService = inject(ChurchService);
  protected hymnService = inject(HymnService);
  protected scheduleService = inject(ScheduleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorshipEventFormGroup = this.worshipEventFormService.createWorshipEventFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareHymn = (o1: IHymn | null, o2: IHymn | null): boolean => this.hymnService.compareHymn(o1, o2);

  compareSchedule = (o1: ISchedule | null, o2: ISchedule | null): boolean => this.scheduleService.compareSchedule(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ worshipEvent }) => {
      this.worshipEvent = worshipEvent;
      if (worshipEvent) {
        this.updateForm(worshipEvent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const worshipEvent = this.worshipEventFormService.getWorshipEvent(this.editForm);
    if (worshipEvent.id !== null) {
      this.subscribeToSaveResponse(this.worshipEventService.update(worshipEvent));
    } else {
      this.subscribeToSaveResponse(this.worshipEventService.create(worshipEvent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorshipEvent>>): void {
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

  protected updateForm(worshipEvent: IWorshipEvent): void {
    this.worshipEvent = worshipEvent;
    this.worshipEventFormService.resetForm(this.editForm, worshipEvent);

    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      worshipEvent.church,
    );
    this.hymnsSharedCollection = this.hymnService.addHymnToCollectionIfMissing<IHymn>(
      this.hymnsSharedCollection,
      ...(worshipEvent.hymns ?? []),
    );
    this.schedulesSharedCollection = this.scheduleService.addScheduleToCollectionIfMissing<ISchedule>(
      this.schedulesSharedCollection,
      ...(worshipEvent.schedules ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.worshipEvent?.church)))
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.hymnService
      .query()
      .pipe(map((res: HttpResponse<IHymn[]>) => res.body ?? []))
      .pipe(map((hymns: IHymn[]) => this.hymnService.addHymnToCollectionIfMissing<IHymn>(hymns, ...(this.worshipEvent?.hymns ?? []))))
      .subscribe((hymns: IHymn[]) => (this.hymnsSharedCollection = hymns));

    this.scheduleService
      .query()
      .pipe(map((res: HttpResponse<ISchedule[]>) => res.body ?? []))
      .pipe(
        map((schedules: ISchedule[]) =>
          this.scheduleService.addScheduleToCollectionIfMissing<ISchedule>(schedules, ...(this.worshipEvent?.schedules ?? [])),
        ),
      )
      .subscribe((schedules: ISchedule[]) => (this.schedulesSharedCollection = schedules));
  }
}
