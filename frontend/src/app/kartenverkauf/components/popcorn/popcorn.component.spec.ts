import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PopcornComponent} from './popcorn.component';

describe('PopcornComponent', () => {
  let component: PopcornComponent;
  let fixture: ComponentFixture<PopcornComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PopcornComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PopcornComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('startet ohne Popcorn (Leerzustand)', () => {
    expect(component.hasPopcorn()).toBe(false);
    expect(component.popcornGesamt()).toBe(0);
  });

  it('addSuggested fügt eine Portion in Empfehlungsgröße hinzu', () => {
    component.addSuggested();

    expect(component.hasPopcorn()).toBe(true);
    expect(component.portionen()).toHaveLength(1);
    expect(component.portionen()[0].groesse).toBe('Mittel');
    expect(component.popcornGesamt()).toBe(500);
  });

  it('Größe ändern aktualisiert den Portionspreis', () => {
    component.addCustom();
    const id = component.portionen()[0].id;

    component.setGroesse(id, 'Groß');

    expect(component.portionen()[0].groesse).toBe('Groß');
    expect(component.popcornGesamt()).toBe(700);
  });

  it('summiert mehrere Portionen', () => {
    component.addCustom();      // Mittel = 500
    component.addCustom();
    component.setGroesse(component.portionen()[1].id, 'Klein'); // 300

    expect(component.popcornGesamt()).toBe(800);
  });

  it('eine Portion entfernen', () => {
    component.addCustom();
    component.addCustom();
    const id = component.portionen()[0].id;

    component.remove(id);

    expect(component.portionen()).toHaveLength(1);
  });

  it('clearAll kehrt in den Leerzustand zurück', () => {
    component.addCustom();

    component.clearAll();

    expect(component.hasPopcorn()).toBe(false);
  });

  it('emittiert die aktuellen Portionen bei jeder Änderung', () => {
    const emissions: number[] = [];
    component.onPopcornGeaendert.subscribe(portionen => emissions.push(portionen.length));

    component.addCustom();
    component.addCustom();
    component.clearAll();

    expect(emissions).toEqual([1, 2, 0]);
  });
});
