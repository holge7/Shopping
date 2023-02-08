package holge.shopping.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends RuntimeException {

	String msg;
	HttpStatus httpStatus;
	
	public UserAlreadyExistsException(String message) {
        super(message);
        this.msg = message;
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
