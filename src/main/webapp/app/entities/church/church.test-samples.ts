import dayjs from 'dayjs/esm';

import { IChurch, NewChurch } from './church.model';

export const sampleWithRequiredData: IChurch = {
  id: 18632,
  name: 'stale',
  cnpj: '01560616140562',
  address: 'gosh till',
  city: 'Aliceboro',
  dateFoundation: dayjs('2024-12-18'),
};

export const sampleWithPartialData: IChurch = {
  id: 5488,
  name: 'develop suspiciously chatter',
  cnpj: '62745341198273',
  address: 'skyline meh serve',
  city: 'Wildermancester',
  dateFoundation: dayjs('2024-12-18'),
  phone: '557.363.1640 x6431',
  website: 'dreamily',
  facebook: 'failing honestly',
  twitter: 'if ugh aboard',
};

export const sampleWithFullData: IChurch = {
  id: 27000,
  name: 'obstruct',
  cnpj: '21210645414743',
  address: 'order swiftly anti',
  city: 'South Queenie',
  dateFoundation: dayjs('2024-12-18'),
  phone: '(867) 244-7999 x03200',
  email: 'Axel7@hotmail.com',
  website: 'travel',
  facebook: 'questionably deer eek',
  instagram: 'stall split',
  twitter: 'zowie so',
  youtube: 'where',
  about: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewChurch = {
  name: 'whereas back hippodrome',
  cnpj: '34358619439705',
  address: 'righteously amid scuttle',
  city: 'Port Abdullah',
  dateFoundation: dayjs('2024-12-18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
