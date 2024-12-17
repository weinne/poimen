import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IHymn } from '../hymn.model';

@Component({
  standalone: true,
  selector: 'jhi-hymn-detail',
  templateUrl: './hymn-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HymnDetailComponent {
  hymn = input<IHymn | null>(null);

  previousState(): void {
    window.history.back();
  }
}
