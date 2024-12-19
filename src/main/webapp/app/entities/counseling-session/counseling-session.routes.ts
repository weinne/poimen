import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CounselingSessionResolve from './route/counseling-session-routing-resolve.service';

const counselingSessionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/counseling-session.component').then(m => m.CounselingSessionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/counseling-session-detail.component').then(m => m.CounselingSessionDetailComponent),
    resolve: {
      counselingSession: CounselingSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/counseling-session-update.component').then(m => m.CounselingSessionUpdateComponent),
    resolve: {
      counselingSession: CounselingSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/counseling-session-update.component').then(m => m.CounselingSessionUpdateComponent),
    resolve: {
      counselingSession: CounselingSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default counselingSessionRoute;
