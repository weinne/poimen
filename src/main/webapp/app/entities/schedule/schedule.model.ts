import dayjs from 'dayjs/esm';
import { IMember } from 'app/entities/member/member.model';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { RoleSchedule } from 'app/entities/enumerations/role-schedule.model';

export interface ISchedule {
  id: number;
  roleType?: keyof typeof RoleSchedule | null;
  notes?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  members?: IMember[] | null;
  worshipEvents?: IWorshipEvent[] | null;
}

export type NewSchedule = Omit<ISchedule, 'id'> & { id: null };
