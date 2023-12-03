import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {DockModule} from "primeng/dock";
import {CardModule} from "primeng/card";
import {NavbarComponent} from "./component/layout/navbar/navbar.component";
import {PageContainerComponent} from "./component/layout/page-container/page-container.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, DockModule, NavbarComponent, PageContainerComponent, CardModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'fintrack-fe';
}
