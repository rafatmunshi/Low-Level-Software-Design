package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import dao.BookDAO;
import exceptions.bookListNotFoundException;
import models.Book;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class provideBooksListServiceTest {
	@Mock
	BookDAO bookDao;

	static BooksListServiceImpl booksListServiceImpl;
	List<Book> books;

	@BeforeEach
	public void init() {
		booksListServiceImpl = new BooksListServiceImpl(bookDao);
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksNull() throws bookListNotFoundException {
		assertNotNull(bookDao);
		when(bookDao.getAllBooks()).thenReturn(null);
		assertThrows(bookListNotFoundException.class, ()->booksListServiceImpl.provideAllBooks(), "it throws not found exception");
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksEmpty() throws bookListNotFoundException {
		when(bookDao.getAllBooks()).thenReturn(new LinkedList<Book>());
		assertEquals(null, booksListServiceImpl.provideAllBooks(), "it returns null");
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksFull() throws bookListNotFoundException {
		when(bookDao.getAllBooks()).thenReturn(utils.displayBookUtil.getAllBooksUtil());
		assertEquals(false, booksListServiceImpl.provideAllBooks().isEmpty(), "it returns full books list");
	}

	
}
