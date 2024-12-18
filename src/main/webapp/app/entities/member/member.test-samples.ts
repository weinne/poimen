import dayjs from 'dayjs/esm';

import { IMember, NewMember } from './member.model';

export const sampleWithRequiredData: IMember = {
  id: 26972,
  firstName: 'Osborne',
  lastName: 'Hirthe',
};

export const sampleWithPartialData: IMember = {
  id: 6066,
  firstName: 'Garett',
  lastName: 'Aufderhar',
  dateOfBirth: dayjs('2024-12-17T23:12'),
};

export const sampleWithFullData: IMember = {
  id: 17319,
  firstName: 'Nestor',
  lastName: 'Dare',
  email: 'Hailee_Mayert@gmail.com',
  phoneNumber: 'if including',
  dateOfBirth: dayjs('2024-12-17T15:56'),
  address: 'duster whereas',
};

export const sampleWithNewData: NewMember = {
  firstName: 'German',
  lastName: 'Schmitt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
