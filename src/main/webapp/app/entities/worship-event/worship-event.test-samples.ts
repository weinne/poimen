import dayjs from 'dayjs/esm';

import { IWorshipEvent, NewWorshipEvent } from './worship-event.model';

export const sampleWithRequiredData: IWorshipEvent = {
  id: 326,
  date: dayjs('2024-12-16T06:13'),
  worshipType: 'BIBLE_STUDY',
};

export const sampleWithPartialData: IWorshipEvent = {
  id: 11000,
  date: dayjs('2024-12-16T02:11'),
  description: 'barring adrenalin',
  worshipType: 'BIBLE_STUDY',
};

export const sampleWithFullData: IWorshipEvent = {
  id: 29049,
  date: dayjs('2024-12-15T20:36'),
  title: 'nerve how',
  description: 'severe',
  worshipType: 'SUNDAY_SERVICE',
};

export const sampleWithNewData: NewWorshipEvent = {
  date: dayjs('2024-12-15T22:49'),
  worshipType: 'PRAYER_MEETING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
