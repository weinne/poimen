import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMinistryMembership } from '../ministry-membership.model';

@Component({
  standalone: true,
  selector: 'jhi-ministry-membership-detail',
  templateUrl: './ministry-membership-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MinistryMembershipDetailComponent {
  ministryMembership = input<IMinistryMembership | null>(null);

  previousState(): void {
    window.history.back();
  }
}