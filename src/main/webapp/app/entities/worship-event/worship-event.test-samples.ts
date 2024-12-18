import dayjs from 'dayjs/esm';

import { IWorshipEvent, NewWorshipEvent } from './worship-event.model';

export const sampleWithRequiredData: IWorshipEvent = {
  id: 326,
  date: dayjs('2024-12-17T11:42'),
  worshipType: 'BIBLE_STUDY',
};

export const sampleWithPartialData: IWorshipEvent = {
  id: 11000,
  date: dayjs('2024-12-17T07:40'),
  description: 'barring adrenalin',
  worshipType: 'BIBLE_STUDY',
};

export const sampleWithFullData: IWorshipEvent = {
  id: 29049,
  date: dayjs('2024-12-17T02:05'),
  title: 'nerve how',
  description: 'severe',
  worshipType: 'SUNDAY_SERVICE',
};

export const sampleWithNewData: NewWorshipEvent = {
  date: dayjs('2024-12-17T04:17'),
  worshipType: 'PRAYER_MEETING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
