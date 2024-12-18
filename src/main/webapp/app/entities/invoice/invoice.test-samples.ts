import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 500,
  number: 'successfully psst requirement',
  issueDate: dayjs('2024-12-17T13:11'),
  totalAmount: 16400.67,
  type: 'oh er',
};

export const sampleWithPartialData: IInvoice = {
  id: 16551,
  number: 'as',
  issueDate: dayjs('2024-12-17T03:13'),
  totalAmount: 13999.36,
  type: 'that yuck anenst',
  invoiceFile: 'ouch creator',
};

export const sampleWithFullData: IInvoice = {
  id: 17304,
  number: 'square likewise option',
  issueDate: dayjs('2024-12-17T05:24'),
  totalAmount: 973.55,
  type: 'than first secrecy',
  supplier: 'exempt',
  invoiceFile: 'luck',
};

export const sampleWithNewData: NewInvoice = {
  number: 'now exhaust',
  issueDate: dayjs('2024-12-17T08:42'),
  totalAmount: 29663.05,
  type: 'the',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
