import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { User } from '../models/user.model';
import { Injectable } from '@angular/core';

@Injectable()
export class UserService {

  private UserAPIBaseUrl = 'http://localhost:8080/spring-rest-api/users/';

  constructor(private http: HttpClient) {
    
   }
  /*
  handleError(error: HttpErrorResponse): Observable<never> {
    switch (error.status) {
      case 0:
        return throwError(Error('Server down: ' + error.status));
      case 400:
        return throwError(Error('Bad Request: ' + error.status));
      case 401:
        return throwError(Error('Unauthorized: ' + error.status));
      case 403:
        return throwError(Error('Forbidden: ' + error.status));
      case 404:
        return throwError(Error('Not found: ' + error.status));
      case 500:
        return throwError(Error('Internal Server Error: ' + error.status));
      default:
        return throwError(Error('An error occurred: ' + error.status));
    }
  }
  */



  handleError(error: HttpErrorResponse): Observable<never> {

    //debugger;
    let errors = "";

    if (error.error.errors != undefined) {
      for (let err of error.error.errors) {
        errors += err + "#";
      }
      errors = errors.substring(0, errors.length - 1);
    } else {
      //debugger;
      errors = "Timestamp: " + error.error.timestamp + "#Status: " + error.error.status + "#Message: " + error.error.message;
    }
    return throwError(Error(errors));
  }





  handleError2(error: HttpErrorResponse): Observable<never> {
    //debugger;
    let errors = "";

    if (error.error.message != undefined) {
      
    errors = "Timestamp: " + error.error.timestamp + "#Status: " + error.error.status + "#Message: " + error.error.message;
      
    } 
    return throwError(Error(errors));
  }





  /*getUsers(): Observable<User[]> {
    return this.http.get<any>(this.UserAPIBaseUrl + 'all').pipe(
      catchError((err: HttpErrorResponse) => this.handleError(err))
    )
  }*/




  /*getUsers(page: number) {
    return this.http.get(this.UserAPIBaseUrl + '?page=' + page);
  }*/





  getUsers(page: number, size: number): Observable<User> {
    return this.http.get<User>(this.UserAPIBaseUrl + 'all' + '?page=' + (page - 1) + '&size=' + size).pipe(
      catchError((err: HttpErrorResponse) => this.handleError2(err))
    );
  }


  countUsers(): Observable<number> {
    return this.http.get<number>(this.UserAPIBaseUrl + "count").pipe(
      catchError((err: HttpErrorResponse) => this.handleError2(err))
    );
  }




  getUserById(id: number): Observable<User> {

    return this.http.get<any>(this.UserAPIBaseUrl + id + '/one').pipe(
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }


  postUser(user: User): Observable<User> {
    const headers = { 'Content-Type': 'application/json' };
    const body = JSON.stringify(user);


    /*
    if (!user.name || !user.email) {
      return throwError(() => new HttpErrorResponse({ status: 406, statusText: 'Not Acceptable' }));
    }
    */

    return this.http.post<User>(this.UserAPIBaseUrl + 'create', body, { headers: headers }).pipe(
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }


  putUserById(user: User): Observable<User> {
    const headers = { 'content-type': 'application/json' };
    const body = JSON.stringify(user);
    const addr = this.UserAPIBaseUrl + user.id + '/put';

    return this.http.put<User>(addr, body, { headers: headers }).pipe(
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }


  patchUserById(user: User): Observable<User> {
    const headers = { 'content-type': 'application/json' };
    const body = JSON.stringify(user);
    const addr = this.UserAPIBaseUrl + user.id + '/patch';

    return this.http.put<User>(addr, body, { headers: headers }).pipe(
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }


  deleteUserById(id: number): Observable<void> {
    return this.http.delete<void>(this.UserAPIBaseUrl + id + '/delete').pipe(
      catchError((err: HttpErrorResponse) => this.handleError(err))
    );
  }
}