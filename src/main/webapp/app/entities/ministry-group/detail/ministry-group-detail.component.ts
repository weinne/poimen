import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMinistryGroup } from '../ministry-group.model';

@Component({
  standalone: true,
  selector: 'jhi-ministry-group-detail',
  templateUrl: './ministry-group-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MinistryGroupDetailComponent {
  ministryGroup = input<IMinistryGroup | null>(null);

  previousState(): void {
    window.history.back();
  }
}
