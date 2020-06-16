export interface IAddress {
  id?: number;
  nomRue?: string;
  ville?: string;
  commun?: string;
  codePostal?: string;
  willaya?: string;
  personId?: number;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public nomRue?: string,
    public ville?: string,
    public commun?: string,
    public codePostal?: string,
    public willaya?: string,
    public personId?: number
  ) {}
}
