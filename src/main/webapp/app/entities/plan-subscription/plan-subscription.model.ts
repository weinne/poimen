import dayjs from 'dayjs/esm';
import { IPlan } from 'app/entities/plan/plan.model';
import { IChurch } from 'app/entities/church/church.model';
import { IUser } from 'app/entities/user/user.model';

export interface IPlanSubscription {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  planName?: string | null;
  plan?: IPlan | null;
  church?: IChurch | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewPlanSubscription = Omit<IPlanSubscription, 'id'> & { id: null };
