export interface Vorstellung {
  uuid: string,
  beginn: string,
  saal: string,
  film: string,
}

export interface Angebot {
  gesamtpreis: Geldbetrag,
  plaetze: Platz[],
  saalplan: Saalplan,
}

export interface Saalplan {
  plaetze: Platz[][];
}

export interface Platz {
  reihe: number,
  platz: number,
  istFrei: boolean,
}

export interface PlatzId {
  reihe: number,
  platz: number,
}

export interface ZusammenhaengendePlaetze {
  plaetze: PlatzId[],
}

export interface Geldbetrag {
  betrag: number,
  waehrung: Waehrung,
}

export enum Waehrung {
  EUR = 'EUR',
}

export interface Preisanfrage {
  vorstellungId: string,
  plaetze: ZusammenhaengendePlaetze,
}

// --- Popcorn ---

export type PopcornGroesse = 'Klein' | 'Mittel' | 'Groß';
export type PopcornGeschmack = 'salzig' | 'süß' | 'gemischt';

// Eine Portion im UI (Menge ist implizit 1 – mehr Popcorn = weitere Portion).
export interface PopcornPortion {
  id: number,
  groesse: PopcornGroesse,
  geschmack: PopcornGeschmack,
}

// Preis je Größe in Cent (passend zum Geldbetrag der Domäne).
export const POPCORN_GROESSE_PREIS: Record<PopcornGroesse, number> = {
  Klein: 300,
  Mittel: 500,
  'Groß': 700,
};

// Mapping der UI-Labels auf die Backend-Enum-Namen.
export const POPCORN_GROESSE_ENUM: Record<PopcornGroesse, string> = {
  Klein: 'KLEIN',
  Mittel: 'MITTEL',
  'Groß': 'GROSS',
};

export const POPCORN_GESCHMACK_ENUM: Record<PopcornGeschmack, string> = {
  salzig: 'SALZIG',
  'süß': 'SUESS',
  gemischt: 'GEMISCHT',
};

// Drahtformat einer Portion für POST …/verkaufsvorgaenge.
export interface PopcornPortionDto {
  groesse: string,
  geschmack: string,
}

export interface Kartenbestellung {
  vorstellungId: string,
  plaetze: ZusammenhaengendePlaetze,
  popcorn: PopcornPortionDto[],
}

export interface Verkaufsvorgang {
  auftragsnummer: string,
  vorstellungId: string,
  plaetze: ZusammenhaengendePlaetze,
  gesamtpreis: Geldbetrag,
}

export interface Zahlungsvorgang {
  id: string,
  anlauf: number,
  betrag: Geldbetrag,
  status: string,
}

export interface Zahlungsstatus {
  status: String
}

export interface Kinokarte {
  film: string,
  beginn: string,
  saal: string,
  reihe: number,
  platz: number,
}


