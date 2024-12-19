import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ChurchService } from '../service/church.service';
import { IChurch } from '../church.model';
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

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected churchService = inject(ChurchService);
  protected churchFormService = inject(ChurchFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChurchFormGroup = this.churchFormService.createChurchFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ church }) => {
      this.church = church;
      if (church) {
        this.updateForm(church);
      }
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
  }
}
