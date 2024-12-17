import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlan } from '../plan.model';
import { PlanService } from '../service/plan.service';

@Component({
  standalone: true,
  templateUrl: './plan-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlanDeleteDialogComponent {
  plan?: IPlan;

  protected planService = inject(PlanService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
