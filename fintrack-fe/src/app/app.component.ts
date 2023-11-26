import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {DockModule} from "primeng/dock";
import {NavbarComponent} from "@app/component/layout/navbar/navbar.component";
import {PageContainerComponent} from "@app/component/layout/page-container/page-container.component";
import {CardModule} from "primeng/card";

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
