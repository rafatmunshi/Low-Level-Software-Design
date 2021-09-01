package services;

import java.util.List;

import exceptions.BookListNotFoundException;
import models.Book;
import models.BorrowStatus;
import models.ReturnStatus;
import models.User;

public interface BookService {
	List<Book> provideAllBooks() throws BookListNotFoundException;

	public BorrowStatus borrowBook(long bookId, User user);

	ReturnStatus returnABook(long bookId, User user);

	ReturnStatus returnBothBooks(User user);
}
