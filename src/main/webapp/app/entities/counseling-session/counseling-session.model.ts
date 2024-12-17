import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { IUser } from 'app/entities/user/user.model';

export interface ICounselingSession {
  id: number;
  date?: dayjs.Dayjs | null;
  notes?: string | null;
  church?: IChurch | null;
  member?: IMember | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewCounselingSession = Omit<ICounselingSession, 'id'> & { id: null };
