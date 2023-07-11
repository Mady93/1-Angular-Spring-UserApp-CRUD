package com.springcrud.user.exceptionsDue;


//import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDue<T> {
	
	
	
	
	//private Instant timestamp;
	//private ZonedDateTime timestamp;
	private String timestamp;
	private Integer status;
	private	T errors;
	private String message;
	private String[] trace;
	private String path;
	
	
	public ErrorResponseDue(Integer status, T errors, String message, String[] trace, String path) {
		//this.timestamp = Instant.now();
		//this.timestamp = ZonedDateTime.now(ZoneId.systemDefault());

		//this.timestamp = ZonedDateTime.now(ZoneId.of("Europe/Rome"));

		/*this.timestamp = ZonedDateTime.now(ZoneId.of("Europe/Rome"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z"));*/

		this.timestamp = ZonedDateTime.now(ZoneId.of("Europe/Rome"))
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS z"));


		this.status = status;
		this.errors = errors;
		this.message = message;
		this.trace = trace;
		this.path = path;
	}

	public ErrorResponseDue(String timestamp,Integer status, T errors, String message, String[] trace, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.errors = errors;
		this.message = message;
		this.trace = trace;
		this.path = path;
	}



}
