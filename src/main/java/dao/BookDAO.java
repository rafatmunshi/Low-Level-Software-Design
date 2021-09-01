package dao;

import java.util.List;

import models.Book;
import models.BookCopies;
import models.BorrowStatus;
import models.ReturnStatus;
import models.User;

public interface BookDAO {
	List<Book> getAllBooks();

	Integer getCopiesOfBook(long bookId);

	Boolean doesBookExist(long bookId);

	BookCopies getBook(long bookId);

	BookCopies removeBook(long bookId);

	void addBookToRepo(Book book);
}
