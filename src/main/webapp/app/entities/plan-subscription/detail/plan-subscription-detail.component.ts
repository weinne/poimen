import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPlanSubscription } from '../plan-subscription.model';

@Component({
  standalone: true,
  selector: 'jhi-plan-subscription-detail',
  templateUrl: './plan-subscription-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlanSubscriptionDetailComponent {
  planSubscription = input<IPlanSubscription | null>(null);

  previousState(): void {
    window.history.back();
  }
}
