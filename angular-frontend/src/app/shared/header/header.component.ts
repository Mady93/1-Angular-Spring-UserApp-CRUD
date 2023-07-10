import { Component, OnInit } from '@angular/core';
import { faAdd, faHome, faNewspaper, faPlateWheat } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  iconaHome = faHome;
  iconaEmployee = faNewspaper;
  iconaInsert = faAdd;
  button = faPlateWheat;

  constructor() { }

  ngOnInit() {
  }

}
