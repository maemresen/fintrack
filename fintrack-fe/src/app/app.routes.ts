import {Routes} from '@angular/router';
import {NotFoundComponent} from "@app/page/not-found/not-found.component";
import {HomeComponent} from "@app/page/home/home.component";

export const routes: Routes = [
  {path: "", component: HomeComponent},
  {path: "emre", component: HomeComponent},
  {path: '**', component: NotFoundComponent}
];
