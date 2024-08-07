import {Component, OnInit} from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {MenuModule} from "primeng/menu";
import {MenuItem} from "primeng/api";
import {BadgeModule} from "primeng/badge";
import {RippleModule} from "primeng/ripple";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MenuModule, BadgeModule, RippleModule, AvatarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        separator: true
      },
      {
        label: 'Documents',
        items: [
          {
            label: 'New',
            icon: 'pi pi-plus',
            shortcut: '⌘+N'
          },
          {
            label: 'Search',
            icon: 'pi pi-search',
            shortcut: '⌘+S'
          }
        ]
      },
      {
        label: 'Profile',
        items: [
          {
            label: 'Settings',
            icon: 'pi pi-cog',
            shortcut: '⌘+O'
          },
          {
            label: 'Messages',
            icon: 'pi pi-inbox',
            badge: '2'
          },
          {
            label: 'Logout',
            icon: 'pi pi-sign-out',
            shortcut: '⌘+Q'
          }
        ]
      },
      {
        separator: true
      }
    ];
  }
}
