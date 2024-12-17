import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IHymn } from 'app/entities/hymn/hymn.model';
import { ISchedule } from 'app/entities/schedule/schedule.model';
import { WorshipType } from 'app/entities/enumerations/worship-type.model';

export interface IWorshipEvent {
  id: number;
  date?: dayjs.Dayjs | null;
  title?: string | null;
  description?: string | null;
  worshipType?: keyof typeof WorshipType | null;
  church?: IChurch | null;
  hymns?: IHymn[] | null;
  schedules?: ISchedule[] | null;
}

export type NewWorshipEvent = Omit<IWorshipEvent, 'id'> & { id: null };
