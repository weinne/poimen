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
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { TransactionService } from '../service/transaction.service';
import { ITransaction } from '../transaction.model';
import { TransactionFormGroup, TransactionFormService } from './transaction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  transaction: ITransaction | null = null;

  churchesSharedCollection: IChurch[] = [];
  membersSharedCollection: IMember[] = [];
  usersSharedCollection: IUser[] = [];

  protected transactionService = inject(TransactionService);
  protected transactionFormService = inject(TransactionFormService);
  protected churchService = inject(ChurchService);
  protected memberService = inject(MemberService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionFormGroup = this.transactionFormService.createTransactionFormGroup();

  compareChurch = (o1: IChurch | null, o2: IChurch | null): boolean => this.churchService.compareChurch(o1, o2);

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      if (transaction) {
        this.updateForm(transaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.transactionFormService.getTransaction(this.editForm);
    if (transaction.id !== null) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.transaction = transaction;
    this.transactionFormService.resetForm(this.editForm, transaction);

    this.churchesSharedCollection = this.churchService.addChurchToCollectionIfMissing<IChurch>(
      this.churchesSharedCollection,
      transaction.church,
    );
    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(
      this.membersSharedCollection,
      transaction.member,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, transaction.user);
  }

  protected loadRelationshipsOptions(): void {
    this.churchService
      .query()
      .pipe(map((res: HttpResponse<IChurch[]>) => res.body ?? []))
      .pipe(map((churches: IChurch[]) => this.churchService.addChurchToCollectionIfMissing<IChurch>(churches, this.transaction?.church)))
      .subscribe((churches: IChurch[]) => (this.churchesSharedCollection = churches));

    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.transaction?.member)))
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.transaction?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}