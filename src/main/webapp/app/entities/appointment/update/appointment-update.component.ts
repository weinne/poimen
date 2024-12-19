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
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { WorshipEventService } from 'app/entities/worship-event/service/worship-event.service';
import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { MinistryGroupService } from 'app/entities/ministry-group/service/ministry-group.service';
import { ICounselingSession } from 'app/entities/counseling-session/counseling-session.model';
import { CounselingSessionService } from 'app/entities/counseling-session/service/counseling-session.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { AppointmentType } from 'app/entities/enumerations/appointment-type.model';
import { AppointmentService } from '../service/appointment.service';
import { IAppointment } from '../appointment.model';
import { AppointmentFormGroup, AppointmentFormService } from './appointment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-appointment-update',
  templateUrl: './appointment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppointmentUpdateComponent implements OnInit {
  isSaving = false;
  appointment: IAppointment | null = null;
  appointmentTypeValues = Object.keys(AppointmentType);

  churchesSharedCollection: IChurch[] = [];
  membersSharedCollection: IMember[] = [];
  worshipEventsSharedCollection: IWorshipEvent[] = [];
  ministryGroupsSharedCollection: IMinistryGroup[] = [];
  counselingSessionsSharedCollection: ICounselingSession[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected appointmentService = inject(AppointmentService);
  protected appointmentFormService = inject(AppointmentFormService);
  protected churchService = inject(ChurchService);
  protected memberService = inject(MemberService);
  protected worshipEventService = inject(WorshipEventService);
  protected ministryGroupService = inject(MinistryGroupService);
  protected counselingSessionService = inject(CounselingSessionService);
  protected applicationUserService = inject(ApplicationUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppointmentFormGroup = this.appointmentFormService.createAppointmentFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  compareWorshipEvent = (o1: IWorshipEvent | null, o2: IWorshipEvent | null): boolean =>
    this.worshipEventService.compareWorshipEvent(o1, o2);

  compareMinistryGroup = (o1: IMinistryGroup | null, o2: IMinistryGroup | null): boolean =>
    this.ministryGroupService.compareMinistryGroup(o1, o2);

  compareCounselingSession = (o1: ICounselingSession | null, o2: ICounselingSession | null): boolean =>
    this.counselingSessionService.compareCounselingSession(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointment }) => {
      this.appointment = appointment;
      if (appointment) {
        this.updateForm(appointment);
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
    const appointment = this.appointmentFormService.getAppointment(this.editForm);
    if (appointment.id !== null) {
      this.subscribeToSaveResponse(this.appointmentService.update(appointment));
    } else {
      this.subscribeToSaveResponse(this.appointmentService.create(appointment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointment>>): void {
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

  protected updateForm(appointment: IAppointment): void {
    this.appointment = appointment;
    this.appointmentFormService.resetForm(this.editForm, appointment);

    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      appointment.church,
    );
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      appointment.member,
    );
    this.worshipEventsSharedCollection = this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(
      this.worshipEventsSharedCollection,
      appointment.service,
    );
    this.ministryGroupsSharedCollection = this.ministryGroupService.addMinistryGroupToCollectionIfMissing<IMinistryGroup>(
      this.ministryGroupsSharedCollection,
      appointment.group,
    );
    this.counselingSessionsSharedCollection = this.counselingSessionService.addCounselingSessionToCollectionIfMissing<ICounselingSession>(
      this.counselingSessionsSharedCollection,
      appointment.counselingSession,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      appointment.user,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.appointment?.church)))
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.appointment?.member)))
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.worshipEventService
      .query()
      .pipe(map((res: HttpResponse<IWorshipEvent[]>) => res.body ?? []))
      .pipe(
        map((worshipEvents: IWorshipEvent[]) =>
          this.worshipEventService.addWorshipEventToCollectionIfMissing<IWorshipEvent>(worshipEvents, this.appointment?.service),
        ),
      )
      .subscribe((worshipEvents: IWorshipEvent[]) => (this.worshipEventsSharedCollection = worshipEvents));

    this.ministryGroupService
      .query()
      .pipe(map((res: HttpResponse<IMinistryGroup[]>) => res.body ?? []))
      .pipe(
        map((ministryGroups: IMinistryGroup[]) =>
          this.ministryGroupService.addMinistryGroupToCollectionIfMissing<IMinistryGroup>(ministryGroups, this.appointment?.group),
        ),
      )
      .subscribe((ministryGroups: IMinistryGroup[]) => (this.ministryGroupsSharedCollection = ministryGroups));

    this.counselingSessionService
      .query()
      .pipe(map((res: HttpResponse<ICounselingSession[]>) => res.body ?? []))
      .pipe(
        map((counselingSessions: ICounselingSession[]) =>
          this.counselingSessionService.addCounselingSessionToCollectionIfMissing<ICounselingSession>(
            counselingSessions,
            this.appointment?.counselingSession,
          ),
        ),
      )
      .subscribe((counselingSessions: ICounselingSession[]) => (this.counselingSessionsSharedCollection = counselingSessions));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(applicationUsers, this.appointment?.user),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }
}
