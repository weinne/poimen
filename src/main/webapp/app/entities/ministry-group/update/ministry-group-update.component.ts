import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IChurch } from 'app/entities/church/church.model';
import { ChurchService } from 'app/entities/church/service/church.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { GroupType } from 'app/entities/enumerations/group-type.model';
import { MinistryGroupService } from '../service/ministry-group.service';
import { IMinistryGroup } from '../ministry-group.model';
import { MinistryGroupFormGroup, MinistryGroupFormService } from './ministry-group-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ministry-group-update',
  templateUrl: './ministry-group-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MinistryGroupUpdateComponent implements OnInit {
  isSaving = false;
  ministryGroup: IMinistryGroup | null = null;
  groupTypeValues = Object.keys(GroupType);

  churchesSharedCollection: IChurch[] = [];
  membersSharedCollection: IMember[] = [];

  protected ministryGroupService = inject(MinistryGroupService);
  protected ministryGroupFormService = inject(MinistryGroupFormService);
  protected churchService = inject(ChurchService);
  protected memberService = inject(MemberService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MinistryGroupFormGroup = this.ministryGroupFormService.createMinistryGroupFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ministryGroup }) => {
      this.ministryGroup = ministryGroup;
      if (ministryGroup) {
        this.updateForm(ministryGroup);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ministryGroup = this.ministryGroupFormService.getMinistryGroup(this.editForm);
    if (ministryGroup.id !== null) {
      this.subscribeToSaveResponse(this.ministryGroupService.update(ministryGroup));
    } else {
      this.subscribeToSaveResponse(this.ministryGroupService.create(ministryGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMinistryGroup>>): void {
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

  protected updateForm(ministryGroup: IMinistryGroup): void {
    this.ministryGroup = ministryGroup;
    this.ministryGroupFormService.resetForm(this.editForm, ministryGroup);

    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      ministryGroup.church,
    );
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      ministryGroup.president,
      ministryGroup.supervisor,
      ...(ministryGroup.members ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.ministryGroup?.church)))
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(
        map((members: IMember[]) =>
          this.memberService.addMemberToCollectionIfMissing<IMember>(
            members,
            this.ministryGroup?.president,
            this.ministryGroup?.supervisor,
            ...(this.ministryGroup?.members ?? []),
          ),
        ),
      )
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));
  }
}
