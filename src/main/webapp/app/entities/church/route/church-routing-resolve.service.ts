import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChurch } from '../church.model';
import { ChurchService } from '../service/church.service';

const churchResolve = (route: ActivatedRouteSnapshot): Observable<null | IChurch> => {
  const id = route.params.id;
  if (id) {
    return inject(ChurchService)
      .find(id)
      .pipe(
        mergeMap((church: HttpResponse<IChurch>) => {
          if (church.body) {
            return of(church.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default churchResolve;
