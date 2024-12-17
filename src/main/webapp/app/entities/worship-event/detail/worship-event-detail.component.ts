import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IWorshipEvent } from '../worship-event.model';

@Component({
  standalone: true,
  selector: 'jhi-worship-event-detail',
  templateUrl: './worship-event-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WorshipEventDetailComponent {
  worshipEvent = input<IWorshipEvent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
