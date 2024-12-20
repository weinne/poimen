import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'poimenApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'application-user',
    data: {
      pageTitle: 'poimenApp.applicationUser.home.title',
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./application-user/application-user.routes'),
  },
  {
    path: 'appointment',
    data: { pageTitle: 'poimenApp.appointment.home.title' },
    loadChildren: () => import('./appointment/appointment.routes'),
  },
  {
    path: 'church',
    data: { pageTitle: 'poimenApp.church.home.title' },
    loadChildren: () => import('./church/church.routes'),
  },
  {
    path: 'counseling-session',
    data: { pageTitle: 'poimenApp.counselingSession.home.title' },
    loadChildren: () => import('./counseling-session/counseling-session.routes'),
  },
  {
    path: 'hymn',
    data: { pageTitle: 'poimenApp.hymn.home.title' },
    loadChildren: () => import('./hymn/hymn.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'poimenApp.invoice.home.title' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'member',
    data: { pageTitle: 'poimenApp.member.home.title' },
    loadChildren: () => import('./member/member.routes'),
  },
  {
    path: 'ministry-group',
    data: { pageTitle: 'poimenApp.ministryGroup.home.title' },
    loadChildren: () => import('./ministry-group/ministry-group.routes'),
  },
  {
    path: 'plan',
    data: { pageTitle: 'poimenApp.plan.home.title' },
    loadChildren: () => import('./plan/plan.routes'),
  },
  {
    path: 'plan-subscription',
    data: { pageTitle: 'poimenApp.planSubscription.home.title' },
    loadChildren: () => import('./plan-subscription/plan-subscription.routes'),
  },
  {
    path: 'task',
    data: { pageTitle: 'poimenApp.task.home.title' },
    loadChildren: () => import('./task/task.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'poimenApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'worship-event',
    data: { pageTitle: 'poimenApp.worshipEvent.home.title' },
    loadChildren: () => import('./worship-event/worship-event.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
