import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: '05d79dc7-593c-42f6-b2fe-961538260980',
  login: '6m',
};

export const sampleWithPartialData: IUser = {
  id: '25207104-328c-480e-be00-f3e6e568176b',
  login: 'eRkbt',
};

export const sampleWithFullData: IUser = {
  id: '7a55c8e7-b9c6-42e4-901a-c5ebc51537b7',
  login: 'r4*m-@9cv1\\{ws3\\|I',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
