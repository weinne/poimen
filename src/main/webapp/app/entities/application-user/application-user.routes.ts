import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ApplicationUserResolve from './route/application-user-routing-resolve.service';

const applicationUserRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/application-user.component').then(m => m.ApplicationUserComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/application-user-detail.component').then(m => m.ApplicationUserDetailComponent),
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/application-user-update.component').then(m => m.ApplicationUserUpdateComponent),
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/application-user-update.component').then(m => m.ApplicationUserUpdateComponent),
    resolve: {
      applicationUser: ApplicationUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default applicationUserRoute;
