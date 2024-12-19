import { IHymn, NewHymn } from './hymn.model';

export const sampleWithRequiredData: IHymn = {
  id: 8849,
  title: 'exactly',
};

export const sampleWithPartialData: IHymn = {
  id: 13003,
  title: 'yuck blah',
  lyricsAuthor: 'boastfully duster',
  musicAuthor: 'ready afterwards',
  link: 'woot',
  youtubeLink: 'than',
  sheetMusic: '../fake-data/blob/hipster.png',
  sheetMusicContentType: 'unknown',
  midi: '../fake-data/blob/hipster.png',
  midiContentType: 'unknown',
  tone: 'frill',
};

export const sampleWithFullData: IHymn = {
  id: 24341,
  title: 'role monthly',
  lyricsAuthor: 'however er what',
  musicAuthor: 'with',
  hymnary: 'by comparison',
  hymnNumber: 'the',
  link: 'brr',
  youtubeLink: 'backbone above own',
  sheetMusic: '../fake-data/blob/hipster.png',
  sheetMusicContentType: 'unknown',
  midi: '../fake-data/blob/hipster.png',
  midiContentType: 'unknown',
  tone: 'an th',
  lyrics: 'under',
};

export const sampleWithNewData: NewHymn = {
  title: 'intent usually enhance',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
