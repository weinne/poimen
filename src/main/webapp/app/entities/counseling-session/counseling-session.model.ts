import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { StatusCounseling } from 'app/entities/enumerations/status-counseling.model';

export interface ICounselingSession {
  id: number;
  subject?: string | null;
  date?: dayjs.Dayjs | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  notes?: string | null;
  counselingTasks?: string | null;
  status?: keyof typeof StatusCounseling | null;
  church?: IChurch | null;
  member?: IMember | null;
  user?: IApplicationUser | null;
}

export type NewCounselingSession = Omit<ICounselingSession, 'id'> & { id: null };
