import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchedule } from '../schedule.model';
import { ScheduleService } from '../service/schedule.service';

const scheduleResolve = (route: ActivatedRouteSnapshot): Observable<null | ISchedule> => {
  const id = route.params.id;
  if (id) {
    return inject(ScheduleService)
      .find(id)
      .pipe(
        mergeMap((schedule: HttpResponse<ISchedule>) => {
          if (schedule.body) {
            return of(schedule.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default scheduleResolve;
