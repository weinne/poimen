import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { MinistryGroupService } from 'app/entities/ministry-group/service/ministry-group.service';
import { IMember } from 'app/entities/member/member.model';
import { MemberService } from 'app/entities/member/service/member.service';
import { RoleMinistry } from 'app/entities/enumerations/role-ministry.model';
import { MinistryMembershipService } from '../service/ministry-membership.service';
import { IMinistryMembership } from '../ministry-membership.model';
import { MinistryMembershipFormGroup, MinistryMembershipFormService } from './ministry-membership-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ministry-membership-update',
  templateUrl: './ministry-membership-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MinistryMembershipUpdateComponent implements OnInit {
  isSaving = false;
  ministryMembership: IMinistryMembership | null = null;
  roleMinistryValues = Object.keys(RoleMinistry);

  ministryGroupsSharedCollection: IMinistryGroup[] = [];
  membersSharedCollection: IMember[] = [];

  protected ministryMembershipService = inject(MinistryMembershipService);
  protected ministryMembershipFormService = inject(MinistryMembershipFormService);
  protected ministryGroupService = inject(MinistryGroupService);
  protected memberService = inject(MemberService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MinistryMembershipFormGroup = this.ministryMembershipFormService.createMinistryMembershipFormGroup();

  compareMinistryGroup = (o1: IMinistryGroup | null, o2: IMinistryGroup | null): boolean =>
    this.ministryGroupService.compareMinistryGroup(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ministryMembership }) => {
      this.ministryMembership = ministryMembership;
      if (ministryMembership) {
        this.updateForm(ministryMembership);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ministryMembership = this.ministryMembershipFormService.getMinistryMembership(this.editForm);
    if (ministryMembership.id !== null) {
      this.subscribeToSaveResponse(this.ministryMembershipService.update(ministryMembership));
    } else {
      this.subscribeToSaveResponse(this.ministryMembershipService.create(ministryMembership));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMinistryMembership>>): void {
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

  protected updateForm(ministryMembership: IMinistryMembership): void {
    this.ministryMembership = ministryMembership;
    this.ministryMembershipFormService.resetForm(this.editForm, ministryMembership);

    this.ministryGroupsSharedCollection = this.ministryGroupService.addMinistryGroupToCollectionIfMissing<IMinistryGroup>(
      this.ministryGroupsSharedCollection,
      ministryMembership.ministryGroup,
    );
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      ministryMembership.member,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ministryGroupService
      .query()
      .pipe(map((res: HttpResponse<IMinistryGroup[]>) => res.body ?? []))
      .pipe(
        map((ministryGroups: IMinistryGroup[]) =>
          this.ministryGroupService.addMinistryGroupToCollectionIfMissing<IMinistryGroup>(
            ministryGroups,
            this.ministryMembership?.ministryGroup,
          ),
        ),
      )
      .subscribe((ministryGroups: IMinistryGroup[]) => (this.ministryGroupsSharedCollection = ministryGroups));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(
        map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.ministryMembership?.member)),
      )
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));
  }
}
