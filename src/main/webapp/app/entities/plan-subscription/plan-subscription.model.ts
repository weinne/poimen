import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IPlan } from 'app/entities/plan/plan.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { PlanSubscriptionStatus } from 'app/entities/enumerations/plan-subscription-status.model';
import { PaymentProvider } from 'app/entities/enumerations/payment-provider.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IPlanSubscription {
  id: number;
  description?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof PlanSubscriptionStatus | null;
  paymentProvider?: keyof typeof PaymentProvider | null;
  paymentStatus?: keyof typeof PaymentStatus | null;
  paymentReference?: string | null;
  church?: IChurch | null;
  plan?: IPlan | null;
  user?: IApplicationUser | null;
}

export type NewPlanSubscription = Omit<IPlanSubscription, 'id'> & { id: null };
