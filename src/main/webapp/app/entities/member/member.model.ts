import dayjs from 'dayjs/esm';
import { IChurch } from 'app/entities/church/church.model';
import { IWorshipEvent } from 'app/entities/worship-event/worship-event.model';
import { IMinistryGroup } from 'app/entities/ministry-group/ministry-group.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { MemberStatus } from 'app/entities/enumerations/member-status.model';
import { MembershipType } from 'app/entities/enumerations/membership-type.model';
import { ExitReason } from 'app/entities/enumerations/exit-reason.model';

export interface IMember {
  id: number;
  name?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  address?: string | null;
  city?: string | null;
  state?: string | null;
  zipCode?: string | null;
  cityOfBirth?: string | null;
  previousReligion?: string | null;
  maritalStatus?: keyof typeof MaritalStatus | null;
  spouseName?: string | null;
  dateOfMarriage?: dayjs.Dayjs | null;
  status?: keyof typeof MemberStatus | null;
  cpf?: string | null;
  rg?: string | null;
  dateOfBaptism?: dayjs.Dayjs | null;
  churchOfBaptism?: string | null;
  dateOfMembership?: dayjs.Dayjs | null;
  typeOfMembership?: keyof typeof MembershipType | null;
  associationMeetingMinutes?: string | null;
  dateOfDeath?: dayjs.Dayjs | null;
  dateOfExit?: dayjs.Dayjs | null;
  exitDestination?: string | null;
  exitReason?: keyof typeof ExitReason | null;
  exitMeetingMinutes?: string | null;
  notes?: string | null;
  church?: IChurch | null;
  playIns?: IWorshipEvent[] | null;
  participateIns?: IWorshipEvent[] | null;
  memberOfs?: IMinistryGroup[] | null;
}

export type NewMember = Omit<IMember, 'id'> & { id: null };
