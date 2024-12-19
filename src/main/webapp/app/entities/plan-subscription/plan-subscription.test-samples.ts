import dayjs from 'dayjs/esm';

import { IPlanSubscription, NewPlanSubscription } from './plan-subscription.model';

export const sampleWithRequiredData: IPlanSubscription = {
  id: 27660,
  description: 'incidentally unfurl sermon',
  startDate: dayjs('2024-12-18'),
  status: 'PAUSED',
  paymentProvider: 'STRIPE',
  paymentStatus: 'PENDING',
};

export const sampleWithPartialData: IPlanSubscription = {
  id: 32490,
  description: 'oh faithfully knowingly',
  startDate: dayjs('2024-12-18'),
  status: 'PAUSED',
  paymentProvider: 'STRIPE',
  paymentStatus: 'FAILED',
};

export const sampleWithFullData: IPlanSubscription = {
  id: 11685,
  description: 'emerge requite deflect',
  startDate: dayjs('2024-12-18'),
  endDate: dayjs('2024-12-18'),
  status: 'CANCELED',
  paymentProvider: 'PAYPAL',
  paymentStatus: 'REFUNDED',
  paymentReference: 'upbeat puritan attend',
};

export const sampleWithNewData: NewPlanSubscription = {
  description: 'marvelous',
  startDate: dayjs('2024-12-18'),
  status: 'ACTIVE',
  paymentProvider: 'PAYPAL',
  paymentStatus: 'REFUNDED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
