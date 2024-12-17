import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlanSubscription } from '../plan-subscription.model';
import { PlanSubscriptionService } from '../service/plan-subscription.service';

@Component({
  standalone: true,
  templateUrl: './plan-subscription-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlanSubscriptionDeleteDialogComponent {
  planSubscription?: IPlanSubscription;

  protected planSubscriptionService = inject(PlanSubscriptionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planSubscriptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
