import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';

export interface IInvoice {
  id: number;
  number?: string | null;
  issueDate?: dayjs.Dayjs | null;
  totalAmount?: number | null;
  type?: string | null;
  supplier?: string | null;
  invoiceFile?: string | null;
  invoiceFileContentType?: string | null;
  church?: IChurch | null;
  transaction?: ITransaction | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
