import { IUser } from 'app/entities/user/user.model';
import { IChurch } from 'app/entities/church/church.model';
import { UserStatus } from 'app/entities/enumerations/user-status.model';

export interface IApplicationUser {
  id: number;
  name?: string | null;
  description?: string | null;
  status?: keyof typeof UserStatus | null;
  internalUser?: Pick<IUser, 'id' | 'login'> | null;
  church?: IChurch | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
