import {Component, computed, input, OnInit, output, signal} from '@angular/core';
import {GeldbetragPipe} from '../../../common/services/geldbetrag.pipe';
import {
  POPCORN_GROESSE_PREIS,
  PopcornGeschmack,
  PopcornGroesse,
  PopcornPortion,
} from '../../dtos/kartenverkauf';

@Component({
  selector: 'app-popcorn',
  imports: [GeldbetragPipe],
  templateUrl: './popcorn.component.html',
  styleUrl: './popcorn.component.css',
})
export class PopcornComponent implements OnInit {

  // Optionale Feinsteuerung des Nudges (siehe Design-Handoff).
  readonly vorauswahl = input<boolean>(false);
  readonly empfehlung = input<PopcornGroesse>('Mittel');

  readonly onPopcornGeaendert = output<PopcornPortion[]>();

  readonly portionen = signal<PopcornPortion[]>([]);
  private nextId = 1;

  readonly hasPopcorn = computed(() => this.portionen().length > 0);
  readonly popcornGesamt = computed(() =>
    this.portionen().reduce((summe, portion) => summe + this.portionPreis(portion), 0));

  readonly empfehlungLabel = computed(() => `${this.empfehlung()} · gemischt`);
  readonly empfehlungPreis = computed(() => POPCORN_GROESSE_PREIS[this.empfehlung()]);

  ngOnInit(): void {
    if (this.vorauswahl()) {
      this.addPortion(this.empfehlung(), 'gemischt');
    }
  }

  portionPreis(portion: PopcornPortion): number {
    return POPCORN_GROESSE_PREIS[portion.groesse];
  }

  istGroesse(portion: PopcornPortion, groesse: PopcornGroesse): boolean {
    return portion.groesse === groesse;
  }

  addSuggested(): void {
    this.addPortion(this.empfehlung(), 'gemischt');
  }

  addCustom(): void {
    this.addPortion('Mittel', 'gemischt');
  }

  setGroesse(id: number, groesse: PopcornGroesse): void {
    this.aktualisiere(this.portionen().map(portion =>
      portion.id === id ? {...portion, groesse} : portion));
  }

  setGeschmack(id: number, geschmack: PopcornGeschmack): void {
    this.aktualisiere(this.portionen().map(portion =>
      portion.id === id ? {...portion, geschmack} : portion));
  }

  remove(id: number): void {
    this.aktualisiere(this.portionen().filter(portion => portion.id !== id));
  }

  clearAll(): void {
    this.aktualisiere([]);
  }

  private addPortion(groesse: PopcornGroesse, geschmack: PopcornGeschmack): void {
    this.aktualisiere([...this.portionen(), {id: this.nextId++, groesse, geschmack}]);
  }

  private aktualisiere(portionen: PopcornPortion[]): void {
    this.portionen.set(portionen);
    this.onPopcornGeaendert.emit(portionen);
  }
}
