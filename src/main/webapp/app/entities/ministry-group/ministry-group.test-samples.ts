import dayjs from 'dayjs/esm';

import { IMinistryGroup, NewMinistryGroup } from './ministry-group.model';

export const sampleWithRequiredData: IMinistryGroup = {
  id: 5074,
  name: 'crackle unexpectedly pfft',
  type: 'DEPARTMENT',
};

export const sampleWithPartialData: IMinistryGroup = {
  id: 5335,
  name: 'but',
  description: 'impractical tough plus',
  type: 'DEACON_BOARD',
};

export const sampleWithFullData: IMinistryGroup = {
  id: 29699,
  name: 'canter yowza',
  description: 'impanel bump',
  establishedDate: dayjs('2024-12-18'),
  type: 'DEACON_BOARD',
};

export const sampleWithNewData: NewMinistryGroup = {
  name: 'lovable always',
  type: 'DEACON_BOARD',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
