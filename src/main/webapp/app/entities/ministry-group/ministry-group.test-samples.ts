import dayjs from 'dayjs/esm';

import { IMinistryGroup, NewMinistryGroup } from './ministry-group.model';

export const sampleWithRequiredData: IMinistryGroup = {
  id: 4888,
  name: 'oh',
  type: 'INTERNAL_SOCIETY',
};

export const sampleWithPartialData: IMinistryGroup = {
  id: 5999,
  name: 'yahoo',
  establishedDate: dayjs('2024-12-16T07:36'),
  leader: 'promptly as',
  type: 'DEACON_BOARD',
};

export const sampleWithFullData: IMinistryGroup = {
  id: 4397,
  name: 'impractical tough plus',
  description: 'vain even ouch',
  establishedDate: dayjs('2024-12-16T15:07'),
  leader: 'ha',
  type: 'DEPARTMENT',
};

export const sampleWithNewData: NewMinistryGroup = {
  name: 'once',
  type: 'CHURCH_COUNCIL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
