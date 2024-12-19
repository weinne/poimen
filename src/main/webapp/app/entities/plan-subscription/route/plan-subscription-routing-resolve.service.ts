import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlanSubscription } from '../plan-subscription.model';
import { PlanSubscriptionService } from '../service/plan-subscription.service';

const planSubscriptionResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlanSubscription> => {
  const id = route.params.id;
  if (id) {
    return inject(PlanSubscriptionService)
      .find(id)
      .pipe(
        mergeMap((planSubscription: HttpResponse<IPlanSubscription>) => {
          if (planSubscription.body) {
            return of(planSubscription.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default planSubscriptionResolve;
