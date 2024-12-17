import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HymnResolve from './route/hymn-routing-resolve.service';

const hymnRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/hymn.component').then(m => m.HymnComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/hymn-detail.component').then(m => m.HymnDetailComponent),
    resolve: {
      hymn: HymnResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/hymn-update.component').then(m => m.HymnUpdateComponent),
    resolve: {
      hymn: HymnResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/hymn-update.component').then(m => m.HymnUpdateComponent),
    resolve: {
      hymn: HymnResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hymnRoute;
