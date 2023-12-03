import {Routes} from '@angular/router';
import {HomeComponent} from "./component/page/home/home.component";
import {NotFoundComponent} from "./component/page/not-found/not-found.component";

export const routes: Routes = [
  {path: "", component: HomeComponent},
  {path: "emre", component: HomeComponent},
  {path: '**', component: NotFoundComponent}
];
