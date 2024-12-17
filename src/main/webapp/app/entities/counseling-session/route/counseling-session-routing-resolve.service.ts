import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICounselingSession } from '../counseling-session.model';
import { CounselingSessionService } from '../service/counseling-session.service';

const counselingSessionResolve = (route: ActivatedRouteSnapshot): Observable<null | ICounselingSession> => {
  const id = route.params.id;
  if (id) {
    return inject(CounselingSessionService)
      .find(id)
      .pipe(
        mergeMap((counselingSession: HttpResponse<ICounselingSession>) => {
          if (counselingSession.body) {
            return of(counselingSession.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default counselingSessionResolve;
