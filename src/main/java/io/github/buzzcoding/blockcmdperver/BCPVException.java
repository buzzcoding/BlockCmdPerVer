package io.github.buzzcoding.blockcmdperver;

public class BCPVException extends Exception {
	private static final long serialVersionUID = 1L;

	//Bring over constructors from Exception class
	public BCPVException() {
		super();
	}

	public BCPVException(String message) {
		super(message);
	}

	public BCPVException(String message, Throwable cause) {
		super(message, cause);
	}

	public BCPVException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BCPVException(Throwable cause) {
		super(cause);
	}
}
