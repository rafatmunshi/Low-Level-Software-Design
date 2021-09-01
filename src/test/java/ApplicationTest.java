import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import controllers.BookController;
import exceptions.bookListNotFoundException;

public class ApplicationTest {
	static LibraryApplication libraryApplication;
	@Mock
	static
	BookController bookController;
	private PrintStream sysOut;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeEach
	public void init() {
		libraryApplication = new LibraryApplication();
		sysOut = System.out;
		System.setOut(new PrintStream(outContent));
	}

	@DisplayName("On call to show options to user")
	@Test
	public void showOptions() throws bookListNotFoundException {
		LibraryApplication.giveOptionsToUser();
		assertEquals(true, outContent.toString().contains("Choose an option-"), "choices are shown");
	}
	@DisplayName("On call to show List Option")
	@Test
	public void showListOption() throws bookListNotFoundException {
		LibraryApplication.giveOptionsToUser();
		assertEquals(true, outContent.toString().contains("View Books in the Library"), "List Books option is shown");
	}
	@DisplayName("On call to show Borrow Option")
	@Test
	public void showBorrowOption() throws bookListNotFoundException {
		LibraryApplication.giveOptionsToUser();
		assertEquals(true, outContent.toString().contains("Borrow a book"), "Borrow Book option is shown");
	}
	@DisplayName("On call to show Return A Book Option")
	@Test
	public void showReturnABookOption() throws bookListNotFoundException {
		LibraryApplication.giveOptionsToUser();
		assertEquals(true, outContent.toString().contains("Return a book"), "Return a book option is shown");
	}
	@DisplayName("On call to show return both books option")
	@Test
	public void showReturnAllOption() throws bookListNotFoundException {
		LibraryApplication.giveOptionsToUser();
		assertEquals(true, outContent.toString().contains("Return both books"), "Return all books option is shown");
	}
	@AfterEach
	public void revertStreams() {
		System.setOut(sysOut);
	}

}
