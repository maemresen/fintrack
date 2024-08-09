import {Component} from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {MenuModule} from "primeng/menu";
import {BadgeModule} from "primeng/badge";
import {RippleModule} from "primeng/ripple";
import {SideMenu} from "@/commons/side-menu/side-menu.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    MenuModule,
    BadgeModule,
    RippleModule,
    AvatarModule,
    SideMenu,
  ],
  templateUrl: './app.component.html',
})
export class AppComponent {
}
