import dayjs from 'dayjs/esm';

import { IChurch, NewChurch } from './church.model';

export const sampleWithRequiredData: IChurch = {
  id: 9453,
  name: 'neaten besides',
  cnpj: 'barring unlike out',
  address: 'zowie enormously',
  city: 'Lonnyton',
  dateFoundation: dayjs('2024-12-16T10:07'),
};

export const sampleWithPartialData: IChurch = {
  id: 5072,
  name: 'promptly gosh',
  cnpj: 'woot',
  address: 'order',
  city: 'Lake Sydni',
  dateFoundation: dayjs('2024-12-15T23:44'),
};

export const sampleWithFullData: IChurch = {
  id: 22282,
  name: 'aha despite',
  cnpj: 'however cardboard thick',
  address: 'heartfelt beyond',
  city: 'Lake Darryl',
  dateFoundation: dayjs('2024-12-15T21:30'),
};

export const sampleWithNewData: NewChurch = {
  name: 'oddly lest aw',
  cnpj: 'aw greedily',
  address: 'till besides',
  city: 'North Ethancester',
  dateFoundation: dayjs('2024-12-15T23:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
