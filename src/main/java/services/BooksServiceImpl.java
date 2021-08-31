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
				return borrowBookUtil(bookId, user);	
			}
		} catch (bookListNotFoundException e) {
			e.printStackTrace();
			return BorrowStatus.ERROR;
		}

	}

	private BorrowStatus borrowBookUtil(long bookId, User user) {
		if(checkBorrowLimitExceed(user))
			return BorrowStatus.BORROW_LIMIT_EXCEEDED;
		if (bookDAO.doesBookExist(bookId)) {
			Integer copies = bookDAO.getCopiesOfBook(bookId);
			if (copies > 1) {
				return borrowFromMultipleCopies(copies, bookId, user);
			}
			if (copies == 1) {
				return borrowFromSingleCopies(bookId, user);
			} else {
				return BorrowStatus.NOT_ENOUGH_COPIES_LEFT;
			}
		} else
			return BorrowStatus.INVALID_BOOK_ID;
		
	}

	private Boolean checkBorrowLimitExceed(User user) {
		return user.getBorrowedBooks().size() == User.getBorrowLimit();
	}

	private BorrowStatus borrowFromSingleCopies(long bookId, User user) {
		List<Book> currentBorrowedList = user.getBorrowedBooks();
		if (isAlreadyBorrowed(currentBorrowedList, bookId))
			return BorrowStatus.CANNOT_BORROW_MORE_COPIES;
		incrementUserBooks(bookId, user);
		bookDAO.removeBook(bookId);
		return BorrowStatus.SUCCESS;
	}

	private void incrementUserBooks(long bookId, User user) {
		List<Book> currentBorrowedList = user.getBorrowedBooks();
		currentBorrowedList.add(bookDAO.getBook(bookId));
		user.setBorrowedBooks(currentBorrowedList);
	}

	private BorrowStatus borrowFromMultipleCopies(Integer copies, long bookId, User user) {
		List<Book> currentBorrowedList = user.getBorrowedBooks();
		if (isAlreadyBorrowed(currentBorrowedList, bookId))
			return BorrowStatus.CANNOT_BORROW_MORE_COPIES;
		incrementUserBooks(bookId, user);
		bookDAO.getBook(bookId).setCopiesOfBook(copies - 1);
		return BorrowStatus.SUCCESS;
	}

	Boolean isAlreadyBorrowed(List<Book> currentBorrowedList, long bookId) {
		for (Book book : currentBorrowedList) {
			if (book.getID() == bookId)
				return true;
		}
		return false;
	}
}
