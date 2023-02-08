package holge.shopping.userservice.exception;

import org.springframework.http.HttpStatus;

public class RolNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HttpStatus httpStatus;
	String msg;
	
    public RolNotFoundException(String message) {
    	super(String.format("Rol [%s] not found", message));
    	this.msg = message;
    	this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
