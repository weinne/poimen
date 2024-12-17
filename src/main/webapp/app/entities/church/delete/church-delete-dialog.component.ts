import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IChurch } from '../church.model';
import { ChurchService } from '../service/church.service';

@Component({
  standalone: true,
  templateUrl: './church-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ChurchDeleteDialogComponent {
  church?: IChurch;

  protected churchService = inject(ChurchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.churchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
