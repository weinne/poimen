import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { IHymn } from 'app/entities/hymn/hymn.model';
import { WorshipType } from 'app/entities/enumerations/worship-type.model';

export interface IWorshipEvent {
  id: number;
  date?: dayjs.Dayjs | null;
  title?: string | null;
  guestPreacher?: string | null;
  description?: string | null;
  callToWorshipText?: string | null;
  confessionOfSinText?: string | null;
  assuranceOfPardonText?: string | null;
  lordSupperText?: string | null;
  benedictionText?: string | null;
  confessionalText?: string | null;
  sermonText?: string | null;
  sermonFile?: string | null;
  sermonFileContentType?: string | null;
  sermonLink?: string | null;
  youtubeLink?: string | null;
  bulletinFile?: string | null;
  bulletinFileContentType?: string | null;
  worshipType?: keyof typeof WorshipType | null;
  church?: IChurch | null;
  preacher?: IMember | null;
  liturgist?: IMember | null;
  hymns?: IHymn[] | null;
  musicians?: IMember[] | null;
  participants?: IMember[] | null;
}

export type NewWorshipEvent = Omit<IWorshipEvent, 'id'> & { id: null };
