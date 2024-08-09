import {Route, Routes} from '@angular/router';
import {HomePageComponent} from "@/pages/home/home-page.component";
import {AboutPageComponent} from "@/pages/about/about-page.component";

export const homeRoute: Route = {
  path: "home",
  title: "Home",
  component: HomePageComponent
}

export const aboutRoute: Route = {
  path: "about",
  title: "About",
  component: AboutPageComponent
}

export const routes: Routes = [
  {
    path: '',
    pathMatch: "full",
    redirectTo: 'home'
  },
  homeRoute,
  aboutRoute
];
