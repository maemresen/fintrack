import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
