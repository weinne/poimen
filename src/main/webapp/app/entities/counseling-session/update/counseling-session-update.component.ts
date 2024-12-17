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
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { CounselingSessionService } from '../service/counseling-session.service';
import { ICounselingSession } from '../counseling-session.model';
import { CounselingSessionFormGroup, CounselingSessionFormService } from './counseling-session-form.service';

@Component({
  standalone: true,
  selector: 'jhi-counseling-session-update',
  templateUrl: './counseling-session-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CounselingSessionUpdateComponent implements OnInit {
  isSaving = false;
  counselingSession: ICounselingSession | null = null;

  churchesSharedCollection: IChurch[] = [];
  membersSharedCollection: IMember[] = [];
  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected counselingSessionService = inject(CounselingSessionService);
  protected counselingSessionFormService = inject(CounselingSessionFormService);
  protected churchService = inject(ChurchService);
  protected memberService = inject(MemberService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CounselingSessionFormGroup = this.counselingSessionFormService.createCounselingSessionFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ counselingSession }) => {
      this.counselingSession = counselingSession;
      if (counselingSession) {
        this.updateForm(counselingSession);
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
    const counselingSession = this.counselingSessionFormService.getCounselingSession(this.editForm);
    if (counselingSession.id !== null) {
      this.subscribeToSaveResponse(this.counselingSessionService.update(counselingSession));
    } else {
      this.subscribeToSaveResponse(this.counselingSessionService.create(counselingSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICounselingSession>>): void {
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

  protected updateForm(counselingSession: ICounselingSession): void {
    this.counselingSession = counselingSession;
    this.counselingSessionFormService.resetForm(this.editForm, counselingSession);

    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      counselingSession.church,
    );
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      counselingSession.member,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, counselingSession.user);
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(
        map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.counselingSession?.church)),
      )
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(
        map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.counselingSession?.member)),
      )
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.counselingSession?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
