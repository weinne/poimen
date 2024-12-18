import dayjs from 'dayjs/esm';

import { IMinistryMembership, NewMinistryMembership } from './ministry-membership.model';

export const sampleWithRequiredData: IMinistryMembership = {
  id: 22871,
  role: 'FIRST_TREASURER',
};

export const sampleWithPartialData: IMinistryMembership = {
  id: 11873,
  role: 'CAUSE_SECRETARY',
  status: false,
};

export const sampleWithFullData: IMinistryMembership = {
  id: 50,
  role: 'VICE_PRESIDENT',
  startDate: dayjs('2024-12-17T09:30'),
  endDate: dayjs('2024-12-17T20:23'),
  status: true,
};

export const sampleWithNewData: NewMinistryMembership = {
  role: 'SECOND_SECRETARY',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
