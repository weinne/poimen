import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WorshipEventResolve from './route/worship-event-routing-resolve.service';

const worshipEventRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/worship-event.component').then(m => m.WorshipEventComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/worship-event-detail.component').then(m => m.WorshipEventDetailComponent),
    resolve: {
      worshipEvent: WorshipEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/worship-event-update.component').then(m => m.WorshipEventUpdateComponent),
    resolve: {
      worshipEvent: WorshipEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/worship-event-update.component').then(m => m.WorshipEventUpdateComponent),
    resolve: {
      worshipEvent: WorshipEventResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default worshipEventRoute;
