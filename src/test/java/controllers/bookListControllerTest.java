package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import exceptions.bookListNotFoundException;
import models.Book;
import models.BorrowStatus;
import models.User;
import services.BookService;

@ExtendWith(MockitoExtension.class)
public class bookListControllerTest {
	@Mock
	BookService bookListService;
	
	static BookController bookListController;
	List<Book> books;
	private PrintStream sysOut;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeEach
	public void init() {
		bookListController = new BookController(bookListService);
		sysOut = System.out;
		System.setOut(new PrintStream(outContent));
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksNull() throws bookListNotFoundException {
		when(bookListService.provideAllBooks()).thenThrow(bookListNotFoundException.class);
		bookListController.displayBooksInLibrary();
		assertEquals(outContent.toString(), "", "it catches and prints exception");
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksEmpty() throws bookListNotFoundException {
		when(bookListService.provideAllBooks()).thenReturn(null);
		bookListController.displayBooksInLibrary();
		assertEquals(outContent.toString(), "The Library is empty" + System.lineSeparator(),
				"it prints library is empty");
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksFull() throws bookListNotFoundException {
		when(bookListService.provideAllBooks()).thenReturn(utils.BookTestUtil.getAllBooksUtil());
		bookListController.displayBooksInLibrary();
		assertEquals(outContent.toString().startsWith("The books presently in the Library are:"), true,
				"it returns full books list");
	}
	
	@DisplayName("On call to borrow a book")
	@Test
	public void testBorrowBook() throws bookListNotFoundException {
		testBorrowLimitExceeded();
		
		testBookBorrowSuccess();
		testNoBooksToBorrow();
	}
	private void testBorrowLimitExceeded() throws bookListNotFoundException {
		User user= new User(1, new LinkedList<Book>());
		when(bookListService.borrowBook((long)1, user)).thenReturn(BorrowStatus.BORROW_LIMIT_EXCEEDED);
		bookListController.borrowBook((long)1, user);
		assertEquals(true, outContent.toString().contains("BORROW_LIMIT_EXCEEDED"));
	}
	private void testBookBorrowSuccess() {
		User user= new User(2, new LinkedList<Book>());
		when(bookListService.borrowBook((long)1, user)).thenReturn(BorrowStatus.BOOK_BORROWED);
		bookListController.borrowBook((long)1, user);
		assertEquals(true, outContent.toString().contains("BOOK_BORROWED"));
	}

	private void testNoBooksToBorrow() {
		User user= new User(3, new LinkedList<Book>());
		when(bookListService.borrowBook((long)1, user)).thenReturn(BorrowStatus.NO_BOOKS_PRESENT);
		bookListController.borrowBook((long)1, user);
		assertEquals(true, outContent.toString().contains("NO_BOOKS_PRESENT"));
	}

	@AfterEach
	public void revertStreams() {
		System.setOut(sysOut);
	}

	
}
