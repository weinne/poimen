import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { StatusTask } from 'app/entities/enumerations/status-task.model';
import { PriorityTask } from 'app/entities/enumerations/priority-task.model';

export interface ITask {
  id: number;
  title?: string | null;
  description?: string | null;
  dueDate?: dayjs.Dayjs | null;
  status?: keyof typeof StatusTask | null;
  priority?: keyof typeof PriorityTask | null;
  notes?: string | null;
  church?: IChurch | null;
  member?: IMember | null;
  user?: IApplicationUser | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
