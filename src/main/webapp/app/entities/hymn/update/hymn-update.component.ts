import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { HymnService } from '../service/hymn.service';
import { IHymn } from '../hymn.model';
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

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
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

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('poimenApp.error', { ...err, key: `error.file.${err.key}` })),
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
      ...(hymn.services ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.worshipEventService
      .query()
      .pipe(map((res: HttpResponse<IWorshipEvent[]>) => res.body ?? []))
      .pipe(
        map((worshipEvents: IWorshipEvent[]) =>
          this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(worshipEvents, ...(this.hymn?.services ?? [])),
        ),
      )
      .subscribe((worshipEvents: IWorshipEvent[]) => (this.worshipEventsSharedCollection = worshipEvents));
  }
}
