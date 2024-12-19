import dayjs from 'dayjs/esm';

import { IAppointment, NewAppointment } from './appointment.model';

export const sampleWithRequiredData: IAppointment = {
  id: 2386,
  subject: 'jaunty an stormy',
  startTime: dayjs('2024-12-18T15:43'),
  appointmentType: 'SERVICE',
};

export const sampleWithPartialData: IAppointment = {
  id: 31375,
  subject: 'meh yum',
  startTime: dayjs('2024-12-18T07:51'),
  endTime: dayjs('2024-12-18T17:23'),
  notes: '../fake-data/blob/hipster.txt',
  appointmentType: 'REHEARSAL',
};

export const sampleWithFullData: IAppointment = {
  id: 21024,
  subject: 'ouch unknown squiggly',
  startTime: dayjs('2024-12-18T15:11'),
  endTime: dayjs('2024-12-18T02:56'),
  notes: '../fake-data/blob/hipster.txt',
  local: 'hydrant',
  appointmentType: 'COUNSELING',
};

export const sampleWithNewData: NewAppointment = {
  subject: 'tray first',
  startTime: dayjs('2024-12-18T01:06'),
  appointmentType: 'VISIT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
