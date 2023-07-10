import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-create-edit',
  templateUrl: './user-create-edit.component.html',
  styleUrls: ['./user-create-edit.component.css']
})
export class UserCreateEditComponent implements OnInit {

  form: FormGroup;
  mode: string;
  pid: number;

  msg: string[];
  showError: boolean = false;

  nameErr: string[];
  emailErr: string[];

  constructor(private formBuilder: FormBuilder, private router: Router, private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit() {


    const currentPath = this.router.url;
    console.log('Current path:', currentPath);

    let x = currentPath.lastIndexOf("/");
    if (x!=0) this.mode = currentPath.substring(1,x);
    else this.mode = currentPath.substring(1);


    if (this.mode == "edit") {

      const id = this.route.snapshot.paramMap.get('id');


      if (id == null) {
        console.log("error");
      }
      else {
        this.pid = parseInt(id, 10);
        this.userService.getUserById(this.pid).subscribe({
          next: (user: User) => {
            this.form.controls.name.setValue(user.name);
            this.form.controls.email.setValue(user.email);

            //this.msg = "";
          },
          error: (err: HttpErrorResponse) => {
            //this.msg = err.message;
            //this.showError = true;
            console.log("error");
          }
        })
      }


      /*
      this.route.queryParamMap.subscribe(params => {
        let id = params.get('id');

        if (id == null) {
          console.log("error");
        }
        else {
          this.pid = parseInt(id, 10);
          this.userService.getUserById(this.pid).subscribe({
            next: (user: User) => {
              this.form.controls.lastName.setValue(user.name);
              this.form.controls.email.setValue(user.email);
            },
            error: (err: HttpErrorResponse) => {

            }
          })
        }



      });
      */

    }



    this.form = this.formBuilder.group({
      name: [
        '',
        [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(50),
          Validators.pattern('^[a-zA-Zà-ù0-9 ]+$'),
        ],
      ],
      email: [
        '',
        [
          Validators.required,
          Validators.pattern('[a-z0-9._%+-]+@[a-z0-9-.]+.[a-z]{2,}$'),
        ],
      ],
    });
  }


  get name(): FormControl {
    return <FormControl>this.form.controls['name'];
  }
  get email(): FormControl {
    return <FormControl>this.form.controls['email'];
  }





  escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); // $& means the whole matched string
  }


  replaceAll(str, find, replace) {
    return str.replace(new RegExp(this.escapeRegExp(find), 'g'), replace);
  }



  action() {
    //debugger;

    if (!this.form.valid) return;


    if (this.mode == "edit") {

      let name = this.form.controls.name.value;
      let email = this.form.controls.email.value;

      let p = new User(this.pid, name, email);

      
      this.userService.putUserById(p).subscribe({
        next: (user: User) => {

          //this.msg = [];
          
        },
        error: (err: HttpErrorResponse) => {

          //debugger;

          let t = err.message.split("#");
          this.nameErr = t.filter(x=>{return x.startsWith("Name")});
          this.emailErr = t.filter(x=>{return x.startsWith("Email")});



          setTimeout(() => {
            this.nameErr = [];
            this.emailErr = [];
          }, 3000);
        },
        complete:()=> {
          console.log('complete putUserById() on user-create-edit');
          this.form.reset();
          this.router.navigate(["/user-list"]);
        }
      });
    }
    else {

      let name = this.form.controls.name.value;
      let email = this.form.controls.email.value;

      let p = new User(0, name, email);

      
      this.userService.postUser(p).subscribe({
        next: (user: User) => {
         
          //this.msg = [];

        },
        error: (err: HttpErrorResponse) => {
          //debugger;
          //this.msg = err.message;

          if (err.message.endsWith(')'))
          {
            console.log(err.message);

            this.msg = this.replaceAll(err.message, "#", "<br>");
            return;
          }



          let t = err.message.split("#");
          this.nameErr = t.filter(x=>{return x.startsWith("Name")});
          this.emailErr = t.filter(x=>{return x.startsWith("Email")});

          
          setTimeout(() => {
            this.nameErr = [];
            this.emailErr = [];
          }, 3000); //  scomparre dopo 3 secondi
          
          
        },
        complete:()=> {
          console.log('complete postUserById() on user-create-edit');
          this.form.reset();
          this.router.navigate(["/user-list"]);
        }
      });
    }
  }


  hideErrorMessage() {
    this.showError = false; 
  }



}

























