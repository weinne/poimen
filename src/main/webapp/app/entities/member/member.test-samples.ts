import dayjs from 'dayjs/esm';

import { IMember, NewMember } from './member.model';

export const sampleWithRequiredData: IMember = {
  id: 9132,
  name: 'yahoo',
  dateOfBirth: dayjs('2024-12-18'),
  maritalStatus: 'WIDOWED',
  status: 'EXCOMMUNICATED',
  cpf: '36088476674',
  rg: 'tusk',
};

export const sampleWithPartialData: IMember = {
  id: 20733,
  name: 'drat',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  email: 'Dessie15@hotmail.com',
  dateOfBirth: dayjs('2024-12-18'),
  address: 'whereas',
  cityOfBirth: 'litter',
  maritalStatus: 'DIVORCED',
  spouseName: 'unlike postbox',
  status: 'DECEASED',
  cpf: '74767105005',
  rg: 'pro',
  churchOfBaptism: 'considering trusting outside',
  dateOfDeath: dayjs('2024-12-18'),
  dateOfExit: dayjs('2024-12-18'),
  exitDestination: 'boohoo',
  exitReason: 'ORDENATION',
};

export const sampleWithFullData: IMember = {
  id: 2747,
  name: 'far',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  email: 'Ramona_Oberbrunner@hotmail.com',
  phoneNumber: 'quip cease',
  dateOfBirth: dayjs('2024-12-18'),
  address: 'lovingly gadzooks',
  city: 'Ramirobury',
  state: 'which',
  zipCode: '97499-2863',
  cityOfBirth: 'finally about',
  previousReligion: 'psst handover instead',
  maritalStatus: 'DIVORCED',
  spouseName: 'that medium jaggedly',
  dateOfMarriage: dayjs('2024-12-18'),
  status: 'PASTOR',
  cpf: '35452777437',
  rg: 'circa',
  dateOfBaptism: dayjs('2024-12-18'),
  churchOfBaptism: 'into',
  dateOfMembership: dayjs('2024-12-18'),
  typeOfMembership: 'BAPTISM_AND_PROFESSION_OF_FAITH',
  associationMeetingMinutes: 'back emotional',
  dateOfDeath: dayjs('2024-12-18'),
  dateOfExit: dayjs('2024-12-18'),
  exitDestination: 'serenade',
  exitReason: 'DEATH',
  exitMeetingMinutes: 'reorganisation plastic',
  notes: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewMember = {
  name: 'zowie',
  dateOfBirth: dayjs('2024-12-18'),
  maritalStatus: 'DIVORCED',
  status: 'COMUNGANT_MEMBER',
  cpf: '36532917758',
  rg: 'astride',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
