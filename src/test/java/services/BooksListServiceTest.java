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
import models.BookCopies;
import models.BorrowStatus;
import models.ReturnStatus;
import models.User;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BooksListServiceTest {
	@Mock
	BookDAO bookDao;

	static BooksServiceImpl booksListServiceImpl;
	List<Book> books;
	static final User user = new User(1, new LinkedList<Book>());

	@BeforeEach
	public void init() {
		booksListServiceImpl = new BooksServiceImpl(bookDao);
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksNull() throws bookListNotFoundException {
		assertNotNull(bookDao);
		when(bookDao.getAllBooks()).thenReturn(null);
		assertThrows(bookListNotFoundException.class, () -> booksListServiceImpl.provideAllBooks(),
				"it should throw not found exception");
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksEmpty() throws bookListNotFoundException {
		when(bookDao.getAllBooks()).thenReturn(new LinkedList<Book>());
		assertEquals(null, booksListServiceImpl.provideAllBooks(), "it should return null");
	}

	@DisplayName("On call to list library books")
	@Test
	public void testLibraryBooksFull() throws bookListNotFoundException {
		when(bookDao.getAllBooks()).thenReturn(utils.BookTestUtil.getAllBooksUtil());
		assertEquals(false, booksListServiceImpl.provideAllBooks().isEmpty(), "it should return full books list");
	}

	@DisplayName("On request to Borrow a book")
	@Test
	public void testBorrowBook() {
		testNoBooksToBorrow();
		testBorrowLimitExceeded();
		testInvalidBook();
		testBorrowNoCopies();
		testRepeatedBorrow();
		testBorrowFromMultipleCopies();
		testBorrowFromSingleCopies();
		testBookBorrowSuccess();
	}

	private void testBorrowLimitExceeded() {
		List<Book> borrowedBooksList = utils.BookTestUtil.getBorrowedBooksTillLimitUtil();
		when(bookDao.getAllBooks()).thenReturn(utils.BookTestUtil.getAllBooksUtil());
		BorrowStatus borrowStatus = booksListServiceImpl.borrowBook(1, new User(1, borrowedBooksList));
		assertEquals(BorrowStatus.BORROW_LIMIT_EXCEEDED, borrowStatus, "it should return borrow limit exceeded");
	}

	private void testNoBooksToBorrow() {
		when(bookDao.getAllBooks()).thenReturn(new LinkedList<Book>());
		BorrowStatus borrowStatus = booksListServiceImpl.borrowBook(1, user);
		assertEquals(BorrowStatus.NO_BOOKS_PRESENT, borrowStatus, "it should return no books present ");
	}

	public void testBookBorrowSuccess() {
		User user = new User(1, new LinkedList<Book>());
		when(bookDao.getCopiesOfBook(1)).thenReturn(1);
		when(bookDao.doesBookExist((long) 1)).thenReturn(true);
		when(bookDao.getBook(1)).thenReturn(new BookCopies(1, "Name1", "Author1", 1));
		assertEquals(BorrowStatus.SUCCESS, booksListServiceImpl.borrowBook((long) 1, user), "it should return success");
		assertEquals(1, user.getBorrowedBooks().get(0).getID(), "it should add to the user's borrowed books list");
	}

	private void testInvalidBook() {
		assertEquals(BorrowStatus.INVALID_BOOK_ID,
				booksListServiceImpl.borrowBook(6, new User(1, new LinkedList<Book>())),
				"it should return invalid book");
	}

	private void testBorrowFromMultipleCopies() {
		when(bookDao.getCopiesOfBook(1)).thenReturn(2);
		when(bookDao.doesBookExist((long) 1)).thenReturn(true);
		when(bookDao.getBook(1)).thenReturn(new BookCopies(1, "Name1", "Author1", 1));
		assertEquals(BorrowStatus.SUCCESS, booksListServiceImpl.borrowBook(1, new User(1, new LinkedList<Book>())),
				"it should return multiple copies");
	}

	private void testBorrowFromSingleCopies() {
		when(bookDao.getCopiesOfBook(2)).thenReturn(1);
		when(bookDao.doesBookExist((long) 2)).thenReturn(true);
		when(bookDao.getBook(2)).thenReturn(new BookCopies(2, "Name1", "Author1", 1));
		assertEquals(BorrowStatus.SUCCESS, booksListServiceImpl.borrowBook(2, new User(1, new LinkedList<Book>())),
				"it should return 1 copy");
	}

	private void testBorrowNoCopies() {
		when(bookDao.getCopiesOfBook(3)).thenReturn(0);
		when(bookDao.doesBookExist((long) 3)).thenReturn(true);
		assertEquals(BorrowStatus.NOT_ENOUGH_COPIES_LEFT,
				booksListServiceImpl.borrowBook(3, new User(1, new LinkedList<Book>())), "it should return 0 copies");
	}

	private void testRepeatedBorrow() {
		assertEquals(true,
				booksListServiceImpl.isAlreadyBorrowed(utils.BookTestUtil.getBorrowedBooksTillLimitUtil(), 1),
				"it should return 0 copies");
	}
	@DisplayName("On call to return a book")
	@Test
	public void testReturnABook() {
		testBookBorrowedForReturn();
		testBookReturnSuccess();
	}

	private void testBookBorrowedForReturn() {
		Book book= new Book(1, null, null);
		List<Book> books=new LinkedList<Book>();
		books.add(book);
		User user= new User(1, books);
		assertEquals(true,booksListServiceImpl.checkValidBook(1, user), "it should validate that book is present");
		assertEquals(false,booksListServiceImpl.checkValidBook(2, user), "it should validate that book is absent");
	}
	private void testBookReturnSuccess() {
		Book book= new Book(1, null, null);
		List<Book> books=new LinkedList<Book>();
		books.add(book);
		User user= new User(1, books);
		assertEquals(ReturnStatus.INVALID_BOOK_ID, booksListServiceImpl.returnABook(2, user), "it should validate that book is absent");
		assertEquals(ReturnStatus.RETURN_SUCCESSFUL, booksListServiceImpl.returnABook(1, user), "it should return successfully");
		assertEquals(0, user.getBorrowedBooks().size());
	}

	@DisplayName("On call to return both books")
	@Test
	public void testReturnBothBooks() {
		testNoBooksToReturn();
		testReturnsBooksSuccess();
	}

	private void testReturnsBooksSuccess() {
		Book book1= new Book(1, null, null);
		Book book2= new Book(2, null, null);
		List<Book> books=new LinkedList<Book>();
		books.add(book1);
		books.add(book2);
		User user= new User(1, books);
		assertEquals(ReturnStatus.RETURN_SUCCESSFUL,booksListServiceImpl.returnBothBooks(user),  "it should return successfully");
		assertEquals(0, user.getBorrowedBooks().size());
	}

	private void testNoBooksToReturn() {
		User user= new User(1, new LinkedList<Book>());
		assertEquals(ReturnStatus.NO_BOOKS_TO_RETURN,booksListServiceImpl.returnBothBooks(user),  "it should signify no books to return");
	}

}
