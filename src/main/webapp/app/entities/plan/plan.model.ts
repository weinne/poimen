export interface IPlan {
  id: number;
  name?: string | null;
  price?: string | null;
  description?: string | null;
  features?: string | null;
  renewalPeriod?: string | null;
}

export type NewPlan = Omit<IPlan, 'id'> & { id: null };
