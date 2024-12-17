import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorshipEvent } from '../worship-event.model';
import { WorshipEventService } from '../service/worship-event.service';

const worshipEventResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorshipEvent> => {
  const id = route.params.id;
  if (id) {
    return inject(WorshipEventService)
      .find(id)
      .pipe(
        mergeMap((worshipEvent: HttpResponse<IWorshipEvent>) => {
          if (worshipEvent.body) {
            return of(worshipEvent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default worshipEventResolve;
