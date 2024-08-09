import {Component, OnInit} from '@angular/core';
import {BadgeModule} from "primeng/badge";
import {MenuModule} from "primeng/menu";
import {MenuItem} from "primeng/api";
import {Ripple} from "primeng/ripple";
import {NgIf} from "@angular/common";
import {AvatarModule} from "primeng/avatar";
import {AppLogo} from "@/assets/svg/app-logo/app-logo";
import {aboutRoute, homeRoute, routes} from "@/app.routes";
import {Route, RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'app-side-menu',
  standalone: true,
  imports: [
    BadgeModule,
    MenuModule,
    Ripple,
    NgIf,
    AvatarModule,
    AppLogo,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './side-menu.component.html',
})
export class SideMenuComponent implements OnInit {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        separator: true
      },
      {
        label: 'Pages',
        items: [
          this.mapToMenuItem(homeRoute),
          this.mapToMenuItem(aboutRoute),
        ]
      },
    ];
  }

  mapToMenuItem = (route: Route): MenuItem => {
    return {
      label: route.title as string,
      routerLink: route.path as string
    }
  }
}
