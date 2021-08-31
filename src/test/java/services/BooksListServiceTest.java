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
import models.BorrowStatus;
import models.User;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BooksListServiceTest {
	@Mock
	BookDAO bookDao;

	static BooksServiceImpl booksListServiceImpl;
	List<Book> books;
	static final User user= new User(1, new LinkedList<Book>());
	@BeforeEach
	public void init() {
		booksListServiceImpl = new BooksServiceImpl(bookDao);
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
		when(bookDao.getAllBooks()).thenReturn(utils.BookTestUtil.getAllBooksUtil());
		assertEquals(false, booksListServiceImpl.provideAllBooks().isEmpty(), "it returns full books list");
	}
	
	@DisplayName("On request to Borrow a book")
	@Test
	public void testBorrowBook() {
		testNoBooksToBorrow();
		testBorrowLimitExceeded();
		testBookBorrowSuccess();
	}

	private void testBorrowLimitExceeded() {
		List<Book> borrowedBooksList= utils.BookTestUtil.getBorrowedBooksTillLimitUtil();
		when(bookDao.getAllBooks()).thenReturn(utils.BookTestUtil.getAllBooksUtil());
		BorrowStatus borrowStatus = booksListServiceImpl.borrowBook(1, new User(1, borrowedBooksList));
		assertEquals(BorrowStatus.BORROW_LIMIT_EXCEEDED, borrowStatus);
	}

	private void testBookBorrowSuccess() {
		when(bookDao.getAllBooks()).thenReturn(utils.BookTestUtil.getAllBooksUtil());
		when(bookDao.borrowBook((long)1, user)).thenReturn(BorrowStatus.BOOK_BORROWED);
		BorrowStatus borrowStatus = booksListServiceImpl.borrowBook((long)1, user);
		assertEquals(BorrowStatus.BOOK_BORROWED, borrowStatus);
	}

	private void testNoBooksToBorrow() {
		when(bookDao.getAllBooks()).thenReturn(new LinkedList<Book>());
		BorrowStatus borrowStatus = booksListServiceImpl.borrowBook(1, user);
		assertEquals(BorrowStatus.NO_BOOKS_PRESENT, borrowStatus);
	}

	
}
