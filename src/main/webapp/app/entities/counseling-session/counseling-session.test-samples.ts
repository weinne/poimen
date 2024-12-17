import dayjs from 'dayjs/esm';

import { ICounselingSession, NewCounselingSession } from './counseling-session.model';

export const sampleWithRequiredData: ICounselingSession = {
  id: 10743,
  date: dayjs('2024-12-16T16:37'),
};

export const sampleWithPartialData: ICounselingSession = {
  id: 15675,
  date: dayjs('2024-12-16T07:04'),
};

export const sampleWithFullData: ICounselingSession = {
  id: 6269,
  date: dayjs('2024-12-16T11:21'),
  notes: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCounselingSession = {
  date: dayjs('2024-12-15T20:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
