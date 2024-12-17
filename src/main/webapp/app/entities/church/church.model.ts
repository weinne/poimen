import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IChurch {
  id: number;
  name?: string | null;
  cnpj?: string | null;
  address?: string | null;
  city?: string | null;
  dateFoundation?: dayjs.Dayjs | null;
  users?: Pick<IUser, 'id'>[] | null;
}

export type NewChurch = Omit<IChurch, 'id'> & { id: null };
