import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import controllers.BookController;
import dao.BookDAO;
import dao.BookDAOInMemImpl;
import exceptions.bookListNotFoundException;
import services.BooksServiceImpl;

public class ApplicationTest {
	static LibraryApplication libraryApplication;
	@Mock
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
	public void testLibraryBooksNull() throws bookListNotFoundException {
		libraryApplication.giveOptionsToUser();
		assertEquals(true, outContent.toString().contains("Choose an option-"), "choices are shown");
	}

	
	@AfterEach
	public void revertStreams() {
		System.setOut(sysOut);
	}

}
