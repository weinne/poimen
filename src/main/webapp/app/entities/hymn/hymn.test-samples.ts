import { IHymn, NewHymn } from './hymn.model';

export const sampleWithRequiredData: IHymn = {
  id: 13676,
  title: 'blah',
};

export const sampleWithPartialData: IHymn = {
  id: 25393,
  title: 'ah yippee yuck',
};

export const sampleWithFullData: IHymn = {
  id: 13937,
  title: 'that uselessly questioningly',
  author: 'underneath deceivingly woot',
  hymnNumber: 'than',
  lyrics: 'frilly',
};

export const sampleWithNewData: NewHymn = {
  title: 'woot fantastic blend',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
