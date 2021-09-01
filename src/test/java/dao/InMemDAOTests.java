package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import models.Book;

public class InMemDAOTests {
	static BookDAOInMemImpl bookDAOInMemImpl;

	@BeforeEach
	public void init() {
		bookDAOInMemImpl = new BookDAOInMemImpl();
	}

	@DisplayName("On call to get all books")
	@Test
	public void testBooksList() {
		assertEquals(utils.BookTestUtil.getAllBooksUtil().get(0).getID(), bookDAOInMemImpl.getAllBooks().get(0).getID(),
				"it should return books list");
	}

	@DisplayName("On getting number of copies of the book")
	@Test
	public void getCopiesOfBook() {
		assertEquals(2, bookDAOInMemImpl.getCopiesOfBook(1), "it should return 2 copies of Book 1");
	}

	@DisplayName("On removing the book")
	@Test
	public void removeBook() {
		bookDAOInMemImpl.removeBook(1);
		assertEquals(null, bookDAOInMemImpl.getBook(1), "it should remove the book from the repo");
	}

	@DisplayName("On checking if book exists in the repository")
	@Test
	public void doesBookExist() {
		assertEquals(true, bookDAOInMemImpl.doesBookExist(1), "it should return true as book exists");
	}

	@DisplayName("On adding a book to Repo")
	@Test
	public void addBookToRepo() {
		bookDAOInMemImpl.addBookToRepo(new Book(1, null, null));
		assertEquals(3, bookDAOInMemImpl.getBook(1).getCopiesOfBook(),
				"it should increment the copies of book with ID 1");
		bookDAOInMemImpl.addBookToRepo(new Book(6, null, null));
		assertEquals(6, bookDAOInMemImpl.getBook(6).getID(), "it should add the book with ID 6");
	}
}
