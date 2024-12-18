import dayjs from 'dayjs/esm';

import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 3783,
  title: 'drat',
};

export const sampleWithPartialData: ITask = {
  id: 11715,
  title: 'positively',
  description: 'instead known',
  completed: false,
};

export const sampleWithFullData: ITask = {
  id: 24616,
  title: 'overstay superb',
  description: 'creamy',
  dueDate: dayjs('2024-12-17T02:34'),
  completed: false,
};

export const sampleWithNewData: NewTask = {
  title: 'begonia taut',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
