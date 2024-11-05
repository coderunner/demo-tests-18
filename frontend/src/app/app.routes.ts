import { Routes } from '@angular/router';
import { AddPageComponent } from './pages/add-page/add-page.component';
import { ViewPageComponent } from './pages/view-page/view-page.component';

export const routes: Routes = [
  { path: 'add', component: AddPageComponent },
  { path: '**', component: ViewPageComponent },
];
