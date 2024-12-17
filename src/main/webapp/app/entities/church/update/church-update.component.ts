import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IChurch } from '../church.model';
import { ChurchService } from '../service/church.service';
import { ChurchFormGroup, ChurchFormService } from './church-form.service';

@Component({
  standalone: true,
  selector: 'jhi-church-update',
  templateUrl: './church-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChurchUpdateComponent implements OnInit {
  isSaving = false;
  church: IChurch | null = null;

  usersSharedCollection: IUser[] = [];

  protected churchService = inject(ChurchService);
  protected churchFormService = inject(ChurchFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChurchFormGroup = this.churchFormService.createChurchFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ church }) => {
      this.church = church;
      if (church) {
        this.updateForm(church);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const church = this.churchFormService.getChurch(this.editForm);
    if (church.id !== null) {
      this.subscribeToSaveResponse(this.churchService.update(church));
    } else {
      this.subscribeToSaveResponse(this.churchService.create(church));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChurch>>): void {
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

  protected updateForm(church: IChurch): void {
    this.church = church;
    this.churchFormService.resetForm(this.editForm, church);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, ...(church.users ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(this.church?.users ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
