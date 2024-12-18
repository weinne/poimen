import dayjs from 'dayjs/esm';

import { ISchedule, NewSchedule } from './schedule.model';

export const sampleWithRequiredData: ISchedule = {
  id: 10305,
  startTime: dayjs('2024-12-17T14:56'),
};

export const sampleWithPartialData: ISchedule = {
  id: 16690,
  notes: 'pecan fluff drat',
  startTime: dayjs('2024-12-17T23:15'),
  endTime: dayjs('2024-12-17T13:36'),
};

export const sampleWithFullData: ISchedule = {
  id: 23796,
  notes: 'buzzing',
  startTime: dayjs('2024-12-17T04:59'),
  endTime: dayjs('2024-12-17T16:02'),
};

export const sampleWithNewData: NewSchedule = {
  startTime: dayjs('2024-12-17T12:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
