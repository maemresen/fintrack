import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MenuItem} from "primeng/api";
import {MenubarModule} from "primeng/menubar";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, MenubarModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        label: 'Home',
        icon: 'pi pi-fw pi-power-off',
        routerLink: ["/"]
      },
      {
        label: 'Not Found',
        icon: 'pi pi-fw pi-power-off',
        routerLink: ["/another"]
      }
    ];
  }
}
