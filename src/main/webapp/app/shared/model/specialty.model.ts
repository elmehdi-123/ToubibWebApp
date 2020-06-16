import { IPerson } from 'app/shared/model/person.model';

export interface ISpecialty {
  id?: number;
  libelle?: string;
  description?: string;
  people?: IPerson[];
}

export class Specialty implements ISpecialty {
  constructor(public id?: number, public libelle?: string, public description?: string, public people?: IPerson[]) {}
}
