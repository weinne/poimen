import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMinistryGroup } from '../ministry-group.model';
import { MinistryGroupService } from '../service/ministry-group.service';

const ministryGroupResolve = (route: ActivatedRouteSnapshot): Observable<null | IMinistryGroup> => {
  const id = route.params.id;
  if (id) {
    return inject(MinistryGroupService)
      .find(id)
      .pipe(
        mergeMap((ministryGroup: HttpResponse<IMinistryGroup>) => {
          if (ministryGroup.body) {
            return of(ministryGroup.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ministryGroupResolve;
