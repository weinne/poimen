import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMinistryMembership } from '../ministry-membership.model';
import { MinistryMembershipService } from '../service/ministry-membership.service';

@Component({
  standalone: true,
  templateUrl: './ministry-membership-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MinistryMembershipDeleteDialogComponent {
  ministryMembership?: IMinistryMembership;

  protected ministryMembershipService = inject(MinistryMembershipService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ministryMembershipService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
