import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWorshipEvent } from '../worship-event.model';
import { WorshipEventService } from '../service/worship-event.service';

@Component({
  standalone: true,
  templateUrl: './worship-event-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorshipEventDeleteDialogComponent {
  worshipEvent?: IWorshipEvent;

  protected worshipEventService = inject(WorshipEventService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.worshipEventService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
