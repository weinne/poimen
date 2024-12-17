import { IPlan, NewPlan } from './plan.model';

export const sampleWithRequiredData: IPlan = {
  id: 3388,
  name: 'unethically loyally',
  price: 'untried',
};

export const sampleWithPartialData: IPlan = {
  id: 21400,
  name: 'pro incandescence while',
  price: 'growing realistic',
};

export const sampleWithFullData: IPlan = {
  id: 24249,
  name: 'until',
  price: 'provided proper recklessly',
};

export const sampleWithNewData: NewPlan = {
  name: 'lean numeric',
  price: 'hammock boo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
