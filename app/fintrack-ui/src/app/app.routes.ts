import {Route, Routes} from '@angular/router';
import {HomePageComponent} from "@/pages/home/home-page.component";
import {AboutPageComponent} from "@/pages/about/about-page.component";

export const homeRoute: Route = {
  title: "Home",
  path: "",
  component: HomePageComponent
}

export const aboutRoute: Route = {
  title: "About",
  path: "about",
  component: AboutPageComponent
}

export const routes: Routes = [
  homeRoute,
  aboutRoute
];
