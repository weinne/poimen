import dayjs from 'dayjs/esm';

import { IPlanSubscription, NewPlanSubscription } from './plan-subscription.model';

export const sampleWithRequiredData: IPlanSubscription = {
  id: 5018,
  startDate: dayjs('2024-12-16T08:54'),
  active: false,
  planName: 'worth requirement glum',
};

export const sampleWithPartialData: IPlanSubscription = {
  id: 20760,
  startDate: dayjs('2024-12-16T17:54'),
  active: true,
  planName: 'anxiously',
};

export const sampleWithFullData: IPlanSubscription = {
  id: 11066,
  startDate: dayjs('2024-12-16T17:50'),
  endDate: dayjs('2024-12-16T02:03'),
  active: true,
  planName: 'slowly bravely',
};

export const sampleWithNewData: NewPlanSubscription = {
  startDate: dayjs('2024-12-16T14:52'),
  active: true,
  planName: 'pace pearl',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
