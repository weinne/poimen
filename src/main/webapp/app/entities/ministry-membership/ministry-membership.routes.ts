import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MinistryMembershipResolve from './route/ministry-membership-routing-resolve.service';

const ministryMembershipRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ministry-membership.component').then(m => m.MinistryMembershipComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ministry-membership-detail.component').then(m => m.MinistryMembershipDetailComponent),
    resolve: {
      ministryMembership: MinistryMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ministry-membership-update.component').then(m => m.MinistryMembershipUpdateComponent),
    resolve: {
      ministryMembership: MinistryMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ministry-membership-update.component').then(m => m.MinistryMembershipUpdateComponent),
    resolve: {
      ministryMembership: MinistryMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ministryMembershipRoute;
