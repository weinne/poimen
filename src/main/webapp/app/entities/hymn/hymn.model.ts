import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';

export interface IHymn {
  id: number;
  title?: string | null;
  lyricsAuthor?: string | null;
  musicAuthor?: string | null;
  hymnary?: string | null;
  hymnNumber?: string | null;
  link?: string | null;
  youtubeLink?: string | null;
  sheetMusic?: string | null;
  sheetMusicContentType?: string | null;
  midi?: string | null;
  midiContentType?: string | null;
  tone?: string | null;
  lyrics?: string | null;
  services?: IWorshipEvent[] | null;
}

export type NewHymn = Omit<IHymn, 'id'> & { id: null };
