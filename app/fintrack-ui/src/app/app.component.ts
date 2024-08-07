import {Component} from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {MenuModule} from "primeng/menu";
import {BadgeModule} from "primeng/badge";
import {RippleModule} from "primeng/ripple";
import {SideMenuContainer} from "./container/side-menu/side-menu.container";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [MenuModule, BadgeModule, RippleModule, AvatarModule, SideMenuContainer],
  templateUrl: './app.component.html',
})
export class AppComponent {
}
