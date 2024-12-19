import dayjs from 'dayjs/esm';

export interface IChurch {
  id: number;
  name?: string | null;
  cnpj?: string | null;
  address?: string | null;
  city?: string | null;
  dateFoundation?: dayjs.Dayjs | null;
  phone?: string | null;
  email?: string | null;
  website?: string | null;
  facebook?: string | null;
  instagram?: string | null;
  twitter?: string | null;
  youtube?: string | null;
  about?: string | null;
}

export type NewChurch = Omit<IChurch, 'id'> & { id: null };
