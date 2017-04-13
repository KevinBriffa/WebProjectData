package se.kebr.exceptions;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 802532382290638624L;

	public ServiceException(String message) {
		super(message);
	}

}
