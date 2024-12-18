import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 25850,
  description: 'pfft',
  amount: 19292.8,
  date: dayjs('2024-12-17T08:22'),
  type: 'diversity manipulate',
};

export const sampleWithPartialData: ITransaction = {
  id: 1765,
  description: 'uselessly sweet',
  amount: 28937.03,
  date: dayjs('2024-12-17T16:45'),
  type: 'indeed of',
};

export const sampleWithFullData: ITransaction = {
  id: 23824,
  description: 'excepting loudly provided',
  amount: 7174.15,
  date: dayjs('2024-12-17T22:32'),
  paymentMethod: 'lift faint',
  type: 'pinstripe unimpressively',
  supplierOrClient: 'minion wasteful gee',
  invoiceFile: 'eek sashay anti',
};

export const sampleWithNewData: NewTransaction = {
  description: 'at bitterly',
  amount: 19063.04,
  date: dayjs('2024-12-17T19:56'),
  type: 'chunter yuck',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
