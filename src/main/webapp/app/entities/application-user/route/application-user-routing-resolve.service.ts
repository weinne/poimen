import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';

const applicationUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IApplicationUser> => {
  const id = route.params.id;
  if (id) {
    return inject(ApplicationUserService)
      .find(id)
      .pipe(
        mergeMap((applicationUser: HttpResponse<IApplicationUser>) => {
          if (applicationUser.body) {
            return of(applicationUser.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default applicationUserResolve;
