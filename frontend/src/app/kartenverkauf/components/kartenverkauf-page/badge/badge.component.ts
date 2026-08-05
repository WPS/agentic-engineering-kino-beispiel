import {Component, input} from '@angular/core';

@Component({
  selector: 'app-badge',
  imports: [],
  templateUrl: './badge.component.html',
})
export class BadgeComponent {

  readonly text = input<string>("");

  readonly checked = input<boolean>(false);

  // 'primary' (grün) für Pflichtschritte, 'accent' (Lachs) für den optionalen Popcorn-Schritt.
  readonly variant = input<'primary' | 'accent'>('primary');

  // Zeigt ein kleines "optional"-Label hinter dem Text (für den Popcorn-Schritt).
  readonly optional = input<boolean>(false);

  // Pflichtschritte zeigen eine Checkbox; der optionale Schritt nicht.
  readonly showCheckbox = input<boolean>(true);

}
