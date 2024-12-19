import dayjs from 'dayjs/esm';

import { IWorshipEvent, NewWorshipEvent } from './worship-event.model';

export const sampleWithRequiredData: IWorshipEvent = {
  id: 26775,
  date: dayjs('2024-12-18T01:22'),
  worshipType: 'SPECIAL_EVENT',
};

export const sampleWithPartialData: IWorshipEvent = {
  id: 14953,
  date: dayjs('2024-12-18T20:34'),
  title: 'place rigidly',
  description: '../fake-data/blob/hipster.txt',
  callToWorshipText: 'unless than',
  sermonText: '../fake-data/blob/hipster.txt',
  sermonFile: '../fake-data/blob/hipster.png',
  sermonFileContentType: 'unknown',
  bulletinFile: '../fake-data/blob/hipster.png',
  bulletinFileContentType: 'unknown',
  worshipType: 'OTHER',
};

export const sampleWithFullData: IWorshipEvent = {
  id: 12599,
  date: dayjs('2024-12-18T22:12'),
  title: 'fat lucky',
  guestPreacher: 'safeguard',
  description: '../fake-data/blob/hipster.txt',
  callToWorshipText: 'provided',
  confessionOfSinText: 'mostly',
  assuranceOfPardonText: 'congregate unruly wretched',
  lordSupperText: 'soon',
  benedictionText: 'drive charming uh-huh',
  confessionalText: 'lest provided',
  sermonText: '../fake-data/blob/hipster.txt',
  sermonFile: '../fake-data/blob/hipster.png',
  sermonFileContentType: 'unknown',
  sermonLink: 'accredit',
  youtubeLink: 'whether for incidentally',
  bulletinFile: '../fake-data/blob/hipster.png',
  bulletinFileContentType: 'unknown',
  worshipType: 'PRAYER_MEETING',
};

export const sampleWithNewData: NewWorshipEvent = {
  date: dayjs('2024-12-18T11:37'),
  worshipType: 'SUNDAY_SERVICE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
