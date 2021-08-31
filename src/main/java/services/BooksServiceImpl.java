package services;

import java.util.List;

import dao.BookDAO;
import exceptions.bookListNotFoundException;
import models.Book;
import models.BorrowStatus;
import models.User;

public class BooksServiceImpl implements BookService {

	protected final BookDAO bookDAO;

	public BooksServiceImpl(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	public List<Book> provideAllBooks() throws bookListNotFoundException {
		List<Book> booksList = bookDAO.getAllBooks();
		if (booksList == null)
			throw new bookListNotFoundException("The books list could not be retrieved at the moment");
		else if (booksList.isEmpty())
			return null;
		else
			return booksList;
	}

	public BorrowStatus borrowBook(long bookId, User user) {
		List<Book> booksInLibrary;
		
		try {
			booksInLibrary = provideAllBooks();
			if (booksInLibrary == null) {
				return BorrowStatus.NO_BOOKS_PRESENT;
			} else {
				if(user.getBorrowedBooks().size()==User.getBorrowLimit()) {
					return BorrowStatus.BORROW_LIMIT_EXCEEDED;
				}
				bookDAO.borrowBook(bookId, user);
				return BorrowStatus.BOOK_BORROWED;
			}
		} catch (bookListNotFoundException e) {
			e.printStackTrace();
			return BorrowStatus.ERROR;
		}

	}
}
