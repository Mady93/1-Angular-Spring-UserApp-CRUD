import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { UserCreateEditComponent } from './components/user-create-edit/user-create-edit.component';
import { HomeComponent } from './components/home/home.component';
import { UserListComponent } from './components/user-list/user-list.component';

const routes: Routes = [
 { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: "home", component: HomeComponent },
  { path: "edit/:id", component: UserCreateEditComponent },
  { path: "create", component: UserCreateEditComponent },
  { path: "user-list", component: UserListComponent}
  /*
  {path: 'search' , component: SearchComponent },
  { path: '**', component: ErrorComponent }
  */
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
