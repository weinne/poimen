import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICounselingSession } from '../counseling-session.model';
import { CounselingSessionService } from '../service/counseling-session.service';

@Component({
  standalone: true,
  templateUrl: './counseling-session-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CounselingSessionDeleteDialogComponent {
  counselingSession?: ICounselingSession;

  protected counselingSessionService = inject(CounselingSessionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.counselingSessionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
