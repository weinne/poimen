import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISchedule } from '../schedule.model';

@Component({
  standalone: true,
  selector: 'jhi-schedule-detail',
  templateUrl: './schedule-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ScheduleDetailComponent {
  schedule = input<ISchedule | null>(null);

  previousState(): void {
    window.history.back();
  }
}
