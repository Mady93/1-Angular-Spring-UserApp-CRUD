package com.springcrud.user.exceptions;

//import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.*;
import java.time.LocalDateTime;


@ControllerAdvice
//@Order(1)
public class GlobalExceptionHandlerController {

    
    
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex) {
	    Date timestamp = new Date();
	    int status = HttpStatus.NOT_FOUND.value();
	    String message = "No resource found in the database";
	    
	    ErrorDetails errorDetails = new ErrorDetails(timestamp, status, message);
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}

	
	
	
	
	
	
    
    @ResponseStatus(HttpStatus.BAD_REQUEST) //.UNPROCESSABLE_ENTITY
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value()); //.UNPROCESSABLE_ENTITY
        errorResponse.setErrors(errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    
    

    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(Exception ex) {
   /* 
    	Date timestamp = new Date();
	    int status = HttpStatus.NOT_FOUND.value();
	    String message = "Email alredy exist";
	    
	    ErrorDetails errorDetails = new ErrorDetails(timestamp, status, message);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
        
       */ 


    
    	List<String> errors = new ArrayList<>();
        errors.add("Email already exist");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setErrors(errors);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
    
  
  
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenerics(Exception ex) {
    	
    	
    	ErrorDetails errorDetails = new ErrorDetails();
        HttpStatus status;
        
    	if (ex instanceof HttpMessageNotReadableException) {
    		
    		status = HttpStatus.BAD_REQUEST;
    		errorDetails.setTimestamp(new Date());
    		errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
    		errorDetails.setMessage(ex.getMessage());
    		
    	} else {
    		
	        status = HttpStatus.INTERNAL_SERVER_ERROR;
	        errorDetails.setTimestamp(new Date());
	        errorDetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        errorDetails.setMessage("INTERNAL SERVER ERROR");
	        
    	}
    	 	
       	return ResponseEntity.status(status).body(errorDetails);
    }
    
    
    
    

   
    
    
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<ErrorDetails> handleBadRequestExceptions(Exception ex) {
    	
    	  String errors = "";
    	  
    	  if (ex instanceof NoHandlerFoundException){
    		  errors += "Invalid URL";
    	  }
          
          ErrorDetails errorDetail = new ErrorDetails();
          errorDetail.setTimestamp(new Date());
          errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
          errorDetail.setMessage(errors);

          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetail);
    }
    
    
    
    
    
    


}


