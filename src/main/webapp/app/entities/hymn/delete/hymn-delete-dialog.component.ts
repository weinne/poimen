import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHymn } from '../hymn.model';
import { HymnService } from '../service/hymn.service';

@Component({
  standalone: true,
  templateUrl: './hymn-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HymnDeleteDialogComponent {
  hymn?: IHymn;

  protected hymnService = inject(HymnService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hymnService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
