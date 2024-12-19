import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IMember } from 'app/entities/member/member.model';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { ICounselingSession } from 'app/entities/counseling-session/counseling-session.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { AppointmentType } from 'app/entities/enumerations/appointment-type.model';

export interface IAppointment {
  id: number;
  subject?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  notes?: string | null;
  local?: string | null;
  appointmentType?: keyof typeof AppointmentType | null;
  church?: IChurch | null;
  member?: IMember | null;
  service?: IWorshipEvent | null;
  group?: IMinistryGroup | null;
  counselingSession?: ICounselingSession | null;
  user?: IApplicationUser | null;
}

export type NewAppointment = Omit<IAppointment, 'id'> & { id: null };
