import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-logo',
  standalone: true,
  imports: [],
  templateUrl: './app-logo.component.svg',
})
export class AppLogoComponent {
  @Input({required: true})
  width!: string

  @Input({required: true})
  height!: string
}
