import {Routes} from '@angular/router';
import {NotFoundComponent} from "@app/component/page/not-found/not-found.component";
import {HomeComponent} from "@app/component/page/home/home.component";

export const routes: Routes = [
  {path: "", component: HomeComponent},
  {path: "emre", component: HomeComponent},
  {path: '**', component: NotFoundComponent}
];
