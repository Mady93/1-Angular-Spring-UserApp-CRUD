package com.springcrud.user.exceptionsDue;


public class ResourceNotFoundExceptionDue extends RuntimeException {

 
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundExceptionDue() {
        super();
    }


	 public ResourceNotFoundExceptionDue(String message) {
        super(message);
    }

    public ResourceNotFoundExceptionDue(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundExceptionDue(Throwable cause) {
        super(cause);
    }

}
