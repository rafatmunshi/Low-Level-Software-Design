package exceptions;

public class BookListNotFoundException extends Exception {
	public BookListNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
