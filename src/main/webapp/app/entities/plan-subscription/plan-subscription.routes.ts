import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PlanSubscriptionResolve from './route/plan-subscription-routing-resolve.service';

const planSubscriptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/plan-subscription.component').then(m => m.PlanSubscriptionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/plan-subscription-detail.component').then(m => m.PlanSubscriptionDetailComponent),
    resolve: {
      planSubscription: PlanSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/plan-subscription-update.component').then(m => m.PlanSubscriptionUpdateComponent),
    resolve: {
      planSubscription: PlanSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/plan-subscription-update.component').then(m => m.PlanSubscriptionUpdateComponent),
    resolve: {
      planSubscription: PlanSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default planSubscriptionRoute;
