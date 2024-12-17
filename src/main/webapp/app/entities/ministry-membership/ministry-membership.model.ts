import dayjs from 'dayjs/esm';
import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { IMember } from 'app/entities/member/member.model';
import { RoleMinistry } from 'app/entities/enumerations/role-ministry.model';

export interface IMinistryMembership {
  id: number;
  role?: keyof typeof RoleMinistry | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: boolean | null;
  ministryGroup?: IMinistryGroup | null;
  member?: IMember | null;
}

export type NewMinistryMembership = Omit<IMinistryMembership, 'id'> & { id: null };
