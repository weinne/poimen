import dayjs from 'dayjs/esm';

import { ICounselingSession, NewCounselingSession } from './counseling-session.model';

export const sampleWithRequiredData: ICounselingSession = {
  id: 10743,
  date: dayjs('2024-12-17T22:06'),
};

export const sampleWithPartialData: ICounselingSession = {
  id: 15675,
  date: dayjs('2024-12-17T12:33'),
};

export const sampleWithFullData: ICounselingSession = {
  id: 6269,
  date: dayjs('2024-12-17T16:50'),
  notes: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCounselingSession = {
  date: dayjs('2024-12-17T01:34'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
