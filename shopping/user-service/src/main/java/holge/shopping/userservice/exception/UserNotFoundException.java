package holge.shopping.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HttpStatus httpStatus;
	String msg;
	
    public UserNotFoundException(String message) {
        super(String.format("User [%s] not found", message));
    	this.msg = message;
    	this.httpStatus = HttpStatus.NOT_FOUND;
    }

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
    
}
