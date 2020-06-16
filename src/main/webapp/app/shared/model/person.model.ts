import { Moment } from 'moment';
import { IAddress } from 'app/shared/model/address.model';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { IAppointment } from 'app/shared/model/appointment.model';
import { TypeCivilite } from 'app/shared/model/enumerations/type-civilite.model';
import { DocteurOrPatientEnum } from 'app/shared/model/enumerations/docteur-or-patient-enum.model';

export interface IPerson {
  id?: number;
  nom?: string;
  prenom?: string;
  numTele?: string;
  eMail?: string;
  dateDeNaissance?: Moment;
  civilite?: TypeCivilite;
  docteurOrPatient?: DocteurOrPatientEnum;
  addresses?: IAddress[];
  userLogin?: string;
  userId?: number;
  specialties?: ISpecialty[];
  appointments?: IAppointment[];
  disponibilties?: IAppointment[];
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public numTele?: string,
    public eMail?: string,
    public dateDeNaissance?: Moment,
    public civilite?: TypeCivilite,
    public docteurOrPatient?: DocteurOrPatientEnum,
    public addresses?: IAddress[],
    public userLogin?: string,
    public userId?: number,
    public specialties?: ISpecialty[],
    public appointments?: IAppointment[],
    public disponibilties?: IAppointment[]
  ) {}
}
