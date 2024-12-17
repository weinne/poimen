import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ChurchResolve from './route/church-routing-resolve.service';

const churchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/church.component').then(m => m.ChurchComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/church-detail.component').then(m => m.ChurchDetailComponent),
    resolve: {
      church: ChurchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/church-update.component').then(m => m.ChurchUpdateComponent),
    resolve: {
      church: ChurchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/church-update.component').then(m => m.ChurchUpdateComponent),
    resolve: {
      church: ChurchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default churchRoute;
