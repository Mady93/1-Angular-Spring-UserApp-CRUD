import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ServerErrorInterceptorService implements HttpInterceptor {
  
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        //debugger;
        if (error.status == 0) {
          
          const customError = new HttpErrorResponse({error: {timestamp: new Date() ,message: 'Error communicating with the server (server offline)', status: 0}});
          return throwError(customError);
        }
        return throwError(error);
      })
    );
  }


  
  
}  





