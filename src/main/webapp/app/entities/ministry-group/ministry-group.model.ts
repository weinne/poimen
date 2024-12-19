import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { GroupType } from 'app/entities/enumerations/group-type.model';

export interface IMinistryGroup {
  id: number;
  name?: string | null;
  description?: string | null;
  establishedDate?: dayjs.Dayjs | null;
  type?: keyof typeof GroupType | null;
  church?: IChurch | null;
  president?: IMember | null;
  supervisor?: IMember | null;
  members?: IMember[] | null;
}

export type NewMinistryGroup = Omit<IMinistryGroup, 'id'> & { id: null };
