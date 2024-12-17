import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';

export interface IHymn {
  id: number;
  title?: string | null;
  author?: string | null;
  hymnNumber?: string | null;
  lyrics?: string | null;
  worshipEvents?: IWorshipEvent[] | null;
}

export type NewHymn = Omit<IHymn, 'id'> & { id: null };
