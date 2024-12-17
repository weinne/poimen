export interface IPlan {
  id: number;
  name?: string | null;
  price?: string | null;
}

export type NewPlan = Omit<IPlan, 'id'> & { id: null };
