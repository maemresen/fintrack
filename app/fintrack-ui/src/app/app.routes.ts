import {Route, Routes} from '@angular/router';
import {HomeComponent} from "@/container/pages/home/home.component";
import {AboutComponent} from "@/container/pages/about/about.component";


export const homeRoute: Route = {
  title: "Home",
  path: "",
  component: HomeComponent
}

export const aboutRoute: Route = {
  title: "About",
  path: "about",
  component: AboutComponent
}

export const routes: Routes = [
  homeRoute,
  aboutRoute
];
