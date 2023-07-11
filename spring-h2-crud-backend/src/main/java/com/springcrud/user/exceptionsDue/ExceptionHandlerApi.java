package com.springcrud.user.exceptionsDue;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
//import com.springcrud.user.exceptions.ResourceNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerApi {




		/* 
	
	@ExceptionHandler(Exception.class)
	public ErrorResponseDue<String> genericException(Exception ex, WebRequest h) {
		Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		String path = getPath(h);
		
        return new ErrorResponseDue<String>(status, ex.getMessage(), "Internal server error", ex.getStackTrace().toString(), path);
    }
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ErrorResponseDue<String> classNotFoundException(ClassNotFoundException ex, WebRequest h) {
		Integer status = HttpStatus.NOT_FOUND.value();
		String path = getPath(h);
		
        return new ErrorResponseDue<String>(status, ex.getMessage(), "Not found", ex.getStackTrace().toString(), path);
    }



	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponseDue<List<String>> validationException(MethodArgumentNotValidException ex, WebRequest h) {

		List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

		Integer status = HttpStatus.BAD_REQUEST.value();
		String path = getPath(h);
		
        return new ErrorResponseDue<List<String>>(status, errors, ex.getMessage(), ex.getStackTrace().toString(), path);
    }

*/


	private static String getPath(WebRequest request) {
		String path = request.getDescription(true);
		path = path.substring(4, path.lastIndexOf(";"));
		return path;
	}




	private String[] getStackTraceAsArray(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String traceString = sw.toString();
		return traceString.split("\\R");
	}



	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundExceptionDue.class)
	public ErrorResponseDue<String> handleResourceNotFoundException(ResourceNotFoundExceptionDue ex, WebRequest request) {

		Integer status = HttpStatus.NOT_FOUND.value();
		String path = getPath(request);

		String[] stackTraceArray = getStackTraceAsArray(ex);
		String errors = HttpStatus.NOT_FOUND.getReasonPhrase();
		String message = ex.getMessage();



		return new ErrorResponseDue<String>(status, errors, message, stackTraceArray, path);
	}




	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponseDue<List<String>> validationException(MethodArgumentNotValidException ex, WebRequest request) {

		List<String> errors = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String errorMessage = error.getDefaultMessage();
			errors.add(errorMessage);
		});

		Integer status = HttpStatus.BAD_REQUEST.value();
		String path = getPath(request);

		String[] stackTraceArray = getStackTraceAsArray(ex);

		String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Rome"))
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS z"));

		return new ErrorResponseDue<List<String>>(timestamp, status, errors, ex.getMessage(), stackTraceArray, path);
	}




	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorResponseDue<List<String>> handleDuplicate(Exception ex, WebRequest request) {

		List<String> errors = new ArrayList<>();
		String errorMessage = "Email already exist";
		errors.add(errorMessage);

		Integer status = HttpStatus.CONFLICT.value();
		String path = getPath(request);
		String[] stackTraceArray = getStackTraceAsArray(ex);

		return new ErrorResponseDue<List<String>>(status, errors, ex.getMessage(), stackTraceArray, path);
	}





	@ExceptionHandler(Exception.class)
	public ErrorResponseDue<String> genericException(Exception ex, WebRequest request) {

		String path = "";
		String[] stackTraceArray;
		String message = "";
		int status;
		String errors = "";

		if (ex instanceof HttpMessageNotReadableException) {

			path = getPath(request);
			stackTraceArray = getStackTraceAsArray(ex);
			message = ex.getMessage();
			status = HttpStatus.BAD_REQUEST.value();
			errors = HttpStatus.BAD_REQUEST.getReasonPhrase();

		} else {

			path = getPath(request);
			stackTraceArray = getStackTraceAsArray(ex);
			message = "Internal server error";
			status = HttpStatus.INTERNAL_SERVER_ERROR.value();
			errors = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		}

		return new ErrorResponseDue<>(status, errors, message, stackTraceArray, path);
	}





	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = { NoHandlerFoundException.class })
	public ErrorResponseDue<String> handleBadRequestExceptions(Exception ex, WebRequest request) {

		String errors = "";
		Integer status = HttpStatus.NOT_FOUND.value();
		String path = getPath(request);
		String[] stackTraceArray = getStackTraceAsArray(ex);

		if (ex instanceof NoHandlerFoundException) {
			errors += "Invalid URL";
		}

		return new ErrorResponseDue<>(status, errors, ex.getMessage(), stackTraceArray, path);
	}




}
