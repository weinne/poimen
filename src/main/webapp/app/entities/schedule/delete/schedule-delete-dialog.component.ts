import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISchedule } from '../schedule.model';
import { ScheduleService } from '../service/schedule.service';

@Component({
  standalone: true,
  templateUrl: './schedule-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ScheduleDeleteDialogComponent {
  schedule?: ISchedule;

  protected scheduleService = inject(ScheduleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scheduleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
