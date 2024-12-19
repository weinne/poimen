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
import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { IHymn } from 'app/entities/hymn/hymn.model';
import { HymnService } from 'app/entities/hymn/service/hymn.service';
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
  membersSharedCollection: IMember[] = [];
  hymnsSharedCollection: IHymn[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected worshipEventService = inject(WorshipEventService);
  protected worshipEventFormService = inject(WorshipEventFormService);
  protected churchService = inject(ChurchService);
  protected memberService = inject(MemberService);
  protected hymnService = inject(HymnService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WorshipEventFormGroup = this.worshipEventFormService.createWorshipEventFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  compareHymn = (o1: IHymn | null, o2: IHymn | null): boolean => this.hymnService.compareHymn(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ worshipEvent }) => {
      this.worshipEvent = worshipEvent;
      if (worshipEvent) {
        this.updateForm(worshipEvent);
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
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      worshipEvent.preacher,
      worshipEvent.liturgist,
      ...(worshipEvent.musicians ?? []),
      ...(worshipEvent.participants ?? []),
    );
    this.hymnsSharedCollection = this.hymnService.addHymnToCollectionIfMissing<IHymn>(
      this.hymnsSharedCollection,
      ...(worshipEvent.hymns ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.worshipEvent?.church)))
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(
        map((members: IMember[]) =>
          this.memberService.addMemberToCollectionIfMissing<IMember>(
            members,
            this.worshipEvent?.preacher,
            this.worshipEvent?.liturgist,
            ...(this.worshipEvent?.musicians ?? []),
            ...(this.worshipEvent?.participants ?? []),
          ),
        ),
      )
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.hymnService
      .query()
      .pipe(map((res: HttpResponse<IHymn[]>) => res.body ?? []))
      .pipe(map((hymns: IHymn[]) => this.hymnService.addHymnToCollectionIfMissing<IHymn>(hymns, ...(this.worshipEvent?.hymns ?? []))))
      .subscribe((hymns: IHymn[]) => (this.hymnsSharedCollection = hymns));
  }
}
