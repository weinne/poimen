import dayjs from 'dayjs/esm';

import { ISchedule, NewSchedule } from './schedule.model';

export const sampleWithRequiredData: ISchedule = {
  id: 18747,
  roleType: 'PREACHER',
  startTime: dayjs('2024-12-16T02:07'),
};

export const sampleWithPartialData: ISchedule = {
  id: 27433,
  roleType: 'PREACHER',
  startTime: dayjs('2024-12-15T20:32'),
};

export const sampleWithFullData: ISchedule = {
  id: 24970,
  roleType: 'LITURGIST',
  notes: 'since nasalise happily',
  startTime: dayjs('2024-12-16T02:18'),
  endTime: dayjs('2024-12-16T03:02'),
};

export const sampleWithNewData: NewSchedule = {
  roleType: 'MUSICIAN',
  startTime: dayjs('2024-12-16T17:01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
