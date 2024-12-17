import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ScheduleResolve from './route/schedule-routing-resolve.service';

const scheduleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/schedule.component').then(m => m.ScheduleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/schedule-detail.component').then(m => m.ScheduleDetailComponent),
    resolve: {
      schedule: ScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/schedule-update.component').then(m => m.ScheduleUpdateComponent),
    resolve: {
      schedule: ScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/schedule-update.component').then(m => m.ScheduleUpdateComponent),
    resolve: {
      schedule: ScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scheduleRoute;
