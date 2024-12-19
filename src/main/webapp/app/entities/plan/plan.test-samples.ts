import { IPlan, NewPlan } from './plan.model';

export const sampleWithRequiredData: IPlan = {
  id: 482,
  name: 'newsprint',
  price: 'zowie unlucky ugh',
};

export const sampleWithPartialData: IPlan = {
  id: 25290,
  name: 'quizzically recovery',
  price: 'woeful why grimy',
  description: 'joyfully',
  features: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPlan = {
  id: 28232,
  name: 'aha unhealthy fooey',
  price: 'uh-huh anti produce',
  description: 'during cosset deliberately',
  features: '../fake-data/blob/hipster.txt',
  renewalPeriod: 'construe',
};

export const sampleWithNewData: NewPlan = {
  name: 'ack',
  price: 'phew',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
