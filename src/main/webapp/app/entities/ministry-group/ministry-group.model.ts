import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { GroupType } from 'app/entities/enumerations/group-type.model';

export interface IMinistryGroup {
  id: number;
  name?: string | null;
  description?: string | null;
  establishedDate?: dayjs.Dayjs | null;
  leader?: string | null;
  type?: keyof typeof GroupType | null;
  church?: IChurch | null;
}

export type NewMinistryGroup = Omit<IMinistryGroup, 'id'> & { id: null };
