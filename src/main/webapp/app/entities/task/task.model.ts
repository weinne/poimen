import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { IUser } from 'app/entities/user/user.model';

export interface ITask {
  id: number;
  title?: string | null;
  description?: string | null;
  dueDate?: dayjs.Dayjs | null;
  completed?: boolean | null;
  church?: IChurch | null;
  member?: IMember | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
