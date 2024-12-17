import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMinistryMembership } from '../ministry-membership.model';
import { MinistryMembershipService } from '../service/ministry-membership.service';

const ministryMembershipResolve = (route: ActivatedRouteSnapshot): Observable<null | IMinistryMembership> => {
  const id = route.params.id;
  if (id) {
    return inject(MinistryMembershipService)
      .find(id)
      .pipe(
        mergeMap((ministryMembership: HttpResponse<IMinistryMembership>) => {
          if (ministryMembership.body) {
            return of(ministryMembership.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ministryMembershipResolve;
