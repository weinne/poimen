import dayjs from 'dayjs/esm';
import { IMember } from 'app/entities/member/member.model';
import { IUser } from 'app/entities/user/user.model';

export interface ISchedule {
  id: number;
  notes?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  members?: IMember[] | null;
  users?: Pick<IUser, 'id'>[] | null;
}

export type NewSchedule = Omit<ISchedule, 'id'> & { id: null };
