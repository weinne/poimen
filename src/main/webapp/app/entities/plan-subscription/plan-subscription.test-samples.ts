import dayjs from 'dayjs/esm';

import { IPlanSubscription, NewPlanSubscription } from './plan-subscription.model';

export const sampleWithRequiredData: IPlanSubscription = {
  id: 5018,
  startDate: dayjs('2024-12-17T14:23'),
  active: false,
  planName: 'worth requirement glum',
};

export const sampleWithPartialData: IPlanSubscription = {
  id: 20760,
  startDate: dayjs('2024-12-17T23:23'),
  active: true,
  planName: 'anxiously',
};

export const sampleWithFullData: IPlanSubscription = {
  id: 11066,
  startDate: dayjs('2024-12-17T23:19'),
  endDate: dayjs('2024-12-17T07:32'),
  active: true,
  planName: 'slowly bravely',
};

export const sampleWithNewData: NewPlanSubscription = {
  startDate: dayjs('2024-12-17T20:20'),
  active: true,
  planName: 'pace pearl',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
