import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { ISchedule } from 'app/entities/schedule/schedule.model';

export interface IMember {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  address?: string | null;
  church?: IChurch | null;
  schedules?: ISchedule[] | null;
}

export type NewMember = Omit<IMember, 'id'> & { id: null };
