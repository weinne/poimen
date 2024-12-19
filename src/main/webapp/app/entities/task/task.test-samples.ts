import dayjs from 'dayjs/esm';

import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 1861,
  title: 'pro whimsical',
  status: 'DONE',
  priority: 'LOW',
};

export const sampleWithPartialData: ITask = {
  id: 23318,
  title: 'which ragged overstay',
  dueDate: dayjs('2024-12-18'),
  status: 'IN_PROGRESS',
  priority: 'MEDIUM',
};

export const sampleWithFullData: ITask = {
  id: 22846,
  title: 'round hm final',
  description: 'faraway during once',
  dueDate: dayjs('2024-12-18'),
  status: 'DONE',
  priority: 'MEDIUM',
  notes: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTask = {
  title: 'decryption brr',
  status: 'PENDING',
  priority: 'HIGH',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
