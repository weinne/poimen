import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'c154f1b5-946a-4124-936e-7a0a1c0c2362',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b5abd66e-966e-470a-b055-057297ca9107',
};

export const sampleWithFullData: IAuthority = {
  name: 'dcc17b15-d5c6-4f27-80ac-47326f55adf6',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
