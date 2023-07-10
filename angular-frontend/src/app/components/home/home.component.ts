import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  title = 'Angular@7 && node@10.16.3 - Full Stack CRUD (Spring && Angular)';

  constructor() { }

  ngOnInit() {
  }

}
