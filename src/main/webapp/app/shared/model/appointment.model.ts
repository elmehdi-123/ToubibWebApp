import { Moment } from 'moment';

export interface IAppointment {
  id?: number;
  motif?: string;
  dateRdv?: Moment;
  personId?: number;
  docteurId?: number;
}

export class Appointment implements IAppointment {
  constructor(public id?: number, public motif?: string, public dateRdv?: Moment, public personId?: number, public docteurId?: number) {}
}
