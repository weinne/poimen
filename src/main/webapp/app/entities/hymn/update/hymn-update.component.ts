import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { IHymn } from '../hymn.model';
import { HymnService } from '../service/hymn.service';
import { HymnFormGroup, HymnFormService } from './hymn-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hymn-update',
  templateUrl: './hymn-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HymnUpdateComponent implements OnInit {
  isSaving = false;
  hymn: IHymn | null = null;

  worshipEventsSharedCollection: IWorshipEvent[] = [];

  protected hymnService = inject(HymnService);
  protected hymnFormService = inject(HymnFormService);
  protected worshipEventService = inject(WorshipEventService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HymnFormGroup = this.hymnFormService.createHymnFormGroup();

  compareWorshipEvent = (o1: IWorshipEvent | null, o2: IWorshipEvent | null): boolean =>
    this.worshipEventService.compareWorshipEvent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hymn }) => {
      this.hymn = hymn;
      if (hymn) {
        this.updateForm(hymn);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hymn = this.hymnFormService.getHymn(this.editForm);
    if (hymn.id !== null) {
      this.subscribeToSaveResponse(this.hymnService.update(hymn));
    } else {
      this.subscribeToSaveResponse(this.hymnService.create(hymn));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHymn>>): void {
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

  protected updateForm(hymn: IHymn): void {
    this.hymn = hymn;
    this.hymnFormService.resetForm(this.editForm, hymn);

    this.worshipEventsSharedCollection = this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
      this.worshipEventsSharedCollection,
      ...(hymn.worshipEvents ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.worshipEventService
      .query()
      .pipe(map((res: HttpResponse<IWorshipEvent[]>) => res.body ?? []))
      .pipe(
        map((worshipEvents: IWorshipEvent[]) =>
          this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(worshipEvents, ...(this.hymn?.worshipEvents ?? [])),
        ),
      )
      .subscribe((worshipEvents: IWorshipEvent[]) => (this.worshipEventsSharedCollection = worshipEvents));
  }
}
