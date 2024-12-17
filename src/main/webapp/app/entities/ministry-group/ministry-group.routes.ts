import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MinistryGroupResolve from './route/ministry-group-routing-resolve.service';

const ministryGroupRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ministry-group.component').then(m => m.MinistryGroupComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ministry-group-detail.component').then(m => m.MinistryGroupDetailComponent),
    resolve: {
      ministryGroup: MinistryGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ministry-group-update.component').then(m => m.MinistryGroupUpdateComponent),
    resolve: {
      ministryGroup: MinistryGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ministry-group-update.component').then(m => m.MinistryGroupUpdateComponent),
    resolve: {
      ministryGroup: MinistryGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ministryGroupRoute;
