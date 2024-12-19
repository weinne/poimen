import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface ITransaction {
  id: number;
  description?: string | null;
  amount?: number | null;
  date?: dayjs.Dayjs | null;
  paymentMethod?: string | null;
  type?: string | null;
  supplierOrClient?: string | null;
  invoiceFile?: string | null;
  church?: IChurch | null;
  member?: IMember | null;
  user?: IApplicationUser | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
