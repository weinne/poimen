import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 30816,
  name: 'grade interior',
  status: 'INACTIVE',
};

export const sampleWithPartialData: IApplicationUser = {
  id: 1461,
  name: 'creak upset declaration',
  description: '../fake-data/blob/hipster.txt',
  status: 'BLOCKED',
};

export const sampleWithFullData: IApplicationUser = {
  id: 9182,
  name: 'however',
  description: '../fake-data/blob/hipster.txt',
  status: 'ACTIVE',
};

export const sampleWithNewData: NewApplicationUser = {
  name: 'ouch shrill',
  status: 'INACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
