import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMinistryGroup } from '../ministry-group.model';
import { MinistryGroupService } from '../service/ministry-group.service';

@Component({
  standalone: true,
  templateUrl: './ministry-group-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MinistryGroupDeleteDialogComponent {
  ministryGroup?: IMinistryGroup;

  protected ministryGroupService = inject(MinistryGroupService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ministryGroupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
