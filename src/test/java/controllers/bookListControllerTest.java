package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import exceptions.bookListNotFoundException;
import models.Book;
import services.BookListService;

@ExtendWith(MockitoExtension.class)
public class bookListControllerTest {
	@Mock
	BookListService bookListService;

	static BookListController bookListController;
	List<Book> books;
	private PrintStream sysOut;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeEach
	public void init() {
		bookListController = new BookListController(bookListService);
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
		when(bookListService.provideAllBooks()).thenReturn(utils.displayBookUtil.getAllBooksUtil());
		bookListController.displayBooksInLibrary();
		assertEquals(outContent.toString().startsWith("The books presently in the Library are:"), true,
				"it returns full books list");
	}

	@AfterEach
	public void revertStreams() {
		System.setOut(sysOut);
	}

}
