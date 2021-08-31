package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sun.tools.javac.util.List;

import exceptions.bookListNotFoundException;
import models.Book;
import models.BorrowStatus;
import models.User;
import services.BooksServiceImpl;

public class InMemDAOTests {
	static BookDAOInMemImpl bookDAOInMemImpl;
	@BeforeEach
	public void init() {
		bookDAOInMemImpl = new BookDAOInMemImpl();
	}
	@DisplayName("On call to get all books")
	@Test
	public void testBooksList() {
		assertEquals(utils.BookTestUtil.getAllBooksUtil().get(0).getID(), bookDAOInMemImpl.getAllBooks().get(0).getID(), "it returns all books list");
	}
	
	@DisplayName("On call to borrow a book")
	@Test
	public void testBorrowBook() {
		User user=new User(1, new LinkedList<Book>());
		LinkedList<Book> newList=new LinkedList<Book>();
		newList.add(new Book(1, "Name1", "Author1"));
		assertEquals(BorrowStatus.SUCCESS, bookDAOInMemImpl.borrowBook(1, user), "it returns success");
		assertEquals(1, user.getBorrowedBooks().get(0).getID(), "it adds to the user's borrowed books list");
		assertEquals(null, bookDAOInMemImpl.bookRepo.get(1), "it removes the book from the repository list");
		assertEquals(BorrowStatus.INVALID_BOOK_ID, bookDAOInMemImpl.borrowBook(6, new User(1, new LinkedList<Book>())), "it returns invalid book");
		
	}
}
