package exceptions;

public class bookListNotFoundException extends Exception {
	public bookListNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
