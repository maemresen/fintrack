import {Component, OnInit} from '@angular/core';
import {BadgeModule} from "primeng/badge";
import {MenuModule} from "primeng/menu";
import {MenuItem} from "primeng/api";
import {Ripple} from "primeng/ripple";
import {NgIf} from "@angular/common";
import {AvatarModule} from "primeng/avatar";
import {AppLogo} from "@/assets/svg/app-logo/app-logo";

@Component({
  selector: 'app-side-menu-container',
  standalone: true,
  imports: [
    BadgeModule,
    MenuModule,
    Ripple,
    NgIf,
    AvatarModule,
    AppLogo
  ],
  templateUrl: './side-menu.container.html',
})
export class SideMenuContainer implements OnInit {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        separator: true
      },
      {
        label: 'Home',
        items: [
          {
            path: '',
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
