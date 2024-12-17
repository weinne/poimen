import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHymn } from '../hymn.model';
import { HymnService } from '../service/hymn.service';

const hymnResolve = (route: ActivatedRouteSnapshot): Observable<null | IHymn> => {
  const id = route.params.id;
  if (id) {
    return inject(HymnService)
      .find(id)
      .pipe(
        mergeMap((hymn: HttpResponse<IHymn>) => {
          if (hymn.body) {
            return of(hymn.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default hymnResolve;
