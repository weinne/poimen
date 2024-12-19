import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 500,
  number: 'successfully psst requirement',
  issueDate: dayjs('2024-12-18T12:11'),
  totalAmount: 16400.67,
  type: 'oh er',
};

export const sampleWithPartialData: IInvoice = {
  id: 16551,
  number: 'as',
  issueDate: dayjs('2024-12-18T02:14'),
  totalAmount: 13999.36,
  type: 'that yuck anenst',
  invoiceFile: '../fake-data/blob/hipster.png',
  invoiceFileContentType: 'unknown',
};

export const sampleWithFullData: IInvoice = {
  id: 11631,
  number: 'guide noisily before',
  issueDate: dayjs('2024-12-18T20:49'),
  totalAmount: 8529.08,
  type: 'when although importance',
  supplier: 'first secrecy beside',
  invoiceFile: '../fake-data/blob/hipster.png',
  invoiceFileContentType: 'unknown',
};

export const sampleWithNewData: NewInvoice = {
  number: 'flimsy',
  issueDate: dayjs('2024-12-18T13:06'),
  totalAmount: 14089.62,
  type: 'kissingly besides',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
