import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 176,
  login: 'EQ91H|@nVj-\\16LAfwm\\SA',
};

export const sampleWithPartialData: IUser = {
  id: 1599,
  login: '-v3P',
};

export const sampleWithFullData: IUser = {
  id: 31217,
  login: 'UF',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
