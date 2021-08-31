package services;

import java.util.List;

import exceptions.bookListNotFoundException;
import models.Book;
import models.BorrowStatus;
import models.User;

public interface BookService {
	List<Book> provideAllBooks() throws bookListNotFoundException;
	public BorrowStatus borrowBook(long bookId, User user);
}
