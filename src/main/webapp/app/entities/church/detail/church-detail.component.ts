import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IChurch } from '../church.model';

@Component({
  standalone: true,
  selector: 'jhi-church-detail',
  templateUrl: './church-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ChurchDetailComponent {
  church = input<IChurch | null>(null);

  previousState(): void {
    window.history.back();
  }
}
