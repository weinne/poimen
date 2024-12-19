import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';

@Component({
  standalone: true,
  templateUrl: './application-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApplicationUserDeleteDialogComponent {
  applicationUser?: IApplicationUser;

  protected applicationUserService = inject(ApplicationUserService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applicationUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
