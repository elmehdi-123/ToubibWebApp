import { Moment } from 'moment';
import { IPerson } from './person.model';

export interface IAppointment {
  id?: number;
  motif?: string;
  dateRdv?: Moment;
  personId?: number;
  docteurId?: number;
  patient?:IPerson;
  docteur?:IPerson;
}

export class Appointment implements IAppointment {
  constructor(public id?: number, public motif?: string, public dateRdv?: Moment, public personId?: number, public docteurId?: number) {}
}
