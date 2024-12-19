import { Component, ElementRef, OnInit, inject } from '@angular/core';
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
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { MinistryGroupService } from 'app/entities/ministry-group/service/ministry-group.service';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { MemberStatus } from 'app/entities/enumerations/member-status.model';
import { MembershipType } from 'app/entities/enumerations/membership-type.model';
import { ExitReason } from 'app/entities/enumerations/exit-reason.model';
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
  maritalStatusValues = Object.keys(MaritalStatus);
  memberStatusValues = Object.keys(MemberStatus);
  membershipTypeValues = Object.keys(MembershipType);
  exitReasonValues = Object.keys(ExitReason);

  churchesSharedCollection: IChurch[] = [];
  worshipEventsSharedCollection: IWorshipEvent[] = [];
  ministryGroupsSharedCollection: IMinistryGroup[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected memberService = inject(MemberService);
  protected memberFormService = inject(MemberFormService);
  protected churchService = inject(ChurchService);
  protected worshipEventService = inject(WorshipEventService);
  protected ministryGroupService = inject(MinistryGroupService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MemberFormGroup = this.memberFormService.createMemberFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareWorshipEvent = (o1: IWorshipEvent | null, o2: IWorshipEvent | null): boolean =>
    this.worshipEventService.compareWorshipEvent(o1, o2);

  compareMinistryGroup = (o1: IMinistryGroup | null, o2: IMinistryGroup | null): boolean =>
    this.ministryGroupService.compareMinistryGroup(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;
      if (member) {
        this.updateForm(member);
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector(`#${idInput}`)) {
      this.elementRef.nativeElement.querySelector(`#${idInput}`).value = null;
    }
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
    this.worshipEventsSharedCollection = this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
      this.worshipEventsSharedCollection,
      ...(member.playIns ?? []),
      ...(member.participateIns ?? []),
    );
    this.ministryGroupsSharedCollection = this.ministryGroupService.addMinistryGroupToCollectionIfMissing<IMinistryGroup>(
      this.ministryGroupsSharedCollection,
      ...(member.memberOfs ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.member?.church)))
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.worshipEventService
      .query()
      .pipe(map((res: HttpResponse<IWorshipEvent[]>) => res.body ?? []))
      .pipe(
        map((worshipEvents: IWorshipEvent[]) =>
          this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
            worshipEvents,
            ...(this.member?.playIns ?? []),
            ...(this.member?.participateIns ?? []),
          ),
        ),
      )
      .subscribe((worshipEvents: IWorshipEvent[]) => (this.worshipEventsSharedCollection = worshipEvents));

    this.ministryGroupService
      .query()
      .pipe(map((res: HttpResponse<IMinistryGroup[]>) => res.body ?? []))
      .pipe(
        map((ministryGroups: IMinistryGroup[]) =>
          this.ministryGroupService.addMinistryGroupToCollectionIfMissing<IMinistryGroup>(
            ministryGroups,
            ...(this.member?.memberOfs ?? []),
          ),
        ),
      )
      .subscribe((ministryGroups: IMinistryGroup[]) => (this.ministryGroupsSharedCollection = ministryGroups));
  }
}
