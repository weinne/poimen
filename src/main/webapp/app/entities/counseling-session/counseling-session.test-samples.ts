import dayjs from 'dayjs/esm';

import { ICounselingSession, NewCounselingSession } from './counseling-session.model';

export const sampleWithRequiredData: ICounselingSession = {
  id: 18364,
  subject: 'sadly inasmuch',
  date: dayjs('2024-12-18'),
  startTime: dayjs('2024-12-18T17:20'),
  status: 'IN_PROGRESS',
};

export const sampleWithPartialData: ICounselingSession = {
  id: 30757,
  subject: 'what',
  date: dayjs('2024-12-18'),
  startTime: dayjs('2024-12-18T23:16'),
  endTime: dayjs('2024-12-18T01:03'),
  notes: '../fake-data/blob/hipster.txt',
  status: 'CANCELED',
};

export const sampleWithFullData: ICounselingSession = {
  id: 26384,
  subject: 'er',
  date: dayjs('2024-12-18'),
  startTime: dayjs('2024-12-19T00:04'),
  endTime: dayjs('2024-12-18T01:54'),
  notes: '../fake-data/blob/hipster.txt',
  counselingTasks: '../fake-data/blob/hipster.txt',
  status: 'DONE',
};

export const sampleWithNewData: NewCounselingSession = {
  subject: 'league unscramble',
  date: dayjs('2024-12-18'),
  startTime: dayjs('2024-12-18T14:30'),
  status: 'IN_PROGRESS',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
