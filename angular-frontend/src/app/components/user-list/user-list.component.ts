import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  Users: User[] = [];
  allUsers: number;
  pagination: number = 1;
  size: number = 2;

  msg:any;
  status:any;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.fetchUsers();
    //console.log(this.fetchUsers());
  }


  escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); // $& means the whole matched string
  }


  replaceAll(str, find, replace) {
    return str.replace(new RegExp(this.escapeRegExp(find), 'g'), replace);
  }


  fetchUsers(){

    this.userService.countUsers().subscribe({
      next: (num: number) => {
        this.allUsers = num;
        this.msg = "";
      },
      error: (err: HttpErrorResponse) => {
        this.allUsers = 0;
        //console.log(err.message);
        //console.log(err);
        //debugger;
        //this.msg = err.message;
        this.msg = this.replaceAll(err.message, "#", "<br>");
      },
      complete: () => {
        console.log("complete countUsers() on user-list")
      }
    })

    //debugger;
    this.userService.getUsers(this.pagination,this.size).subscribe({
      next:(res:any) => {
          //debugger;
          this.Users = res;
          console.log(res);

          this.msg ="";
      },
      error:(err:HttpErrorResponse) => {

        console.log(err.message);

        this.msg = this.replaceAll(err.message, "#", "<br>");
      },
      complete: () => {
        console.log("complete fetchUsers() on user-list")
      }
    })
  }




  renderPage(event: number) {
    this.pagination = (event);
    this.fetchUsers();
  }




  deleteById(id: number) {
    this.userService.deleteUserById(id).subscribe({
      next: () => {
        this.msg = "";
        this.fetchUsers();
      },
      error:(err:HttpErrorResponse)=>{
        this.msg = err.message;
      },
      complete:()=>{
        console.log('complete deleteById() on user-list');
      }
    })
  }



}
