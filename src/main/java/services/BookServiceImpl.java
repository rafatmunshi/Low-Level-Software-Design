package services;

import java.util.LinkedList;
import java.util.List;

import dao.BookDAO;
import exceptions.BookListNotFoundException;
import models.Book;
import models.BorrowStatus;
import models.ReturnStatus;
import models.User;

public class BookServiceImpl implements BookService {

	protected final BookDAO bookDAO;

	public BookServiceImpl(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	@Override
	public List<Book> provideAllBooks() throws BookListNotFoundException {
		List<Book> booksList = bookDAO.getAllBooks();
		if (booksList == null)
			throw new BookListNotFoundException("The books list could not be retrieved at the moment");
		else if (booksList.isEmpty())
			return null;
		else
			return booksList;
	}

	@Override
	public BorrowStatus borrowBook(long bookId, User user) {
		List<Book> booksInLibrary;

		try {
			booksInLibrary = provideAllBooks();
			if (booksInLibrary == null) {
				return BorrowStatus.NO_BOOKS_PRESENT;
			} else {
				return borrowBookUtil(bookId, user);
			}
		} catch (BookListNotFoundException e) {
			e.printStackTrace();
			return BorrowStatus.ERROR;
		}

	}

	private BorrowStatus borrowBookUtil(long bookId, User user) {
		if (checkBorrowLimitExceed(user))
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

	@Override
	public ReturnStatus returnABook(long bookId, User user) {
		if (!checkValidBook(bookId, user))
			return ReturnStatus.INVALID_BOOK_ID;
		else {
			// these two have to be atomic else book can be lost
			addBookToRepo(bookId, user);
			removeFromBorrowedList(bookId, user);
			return ReturnStatus.RETURN_SUCCESSFUL;
		}
	}

	private void addBookToRepo(long bookId, User user) {
		List<Book> currentBorrowedList = user.getBorrowedBooks();
		for (Book book : currentBorrowedList)
			if (book.getID() == bookId)
				bookDAO.addBookToRepo(book);
	}

	boolean checkValidBook(long bookId, User user) {
		List<Book> currentBorrowedList = user.getBorrowedBooks();
		if (isAlreadyBorrowed(currentBorrowedList, bookId))
			return true;
		return false;
	}

	private void removeFromBorrowedList(long bookId, User user) {
		List<Book> currentBorrowedList = user.getBorrowedBooks();
		int i = 0;
		for (; i < currentBorrowedList.size(); i++) {
			if (currentBorrowedList.get(i).getID() == bookId)
				break;
		}
		currentBorrowedList.remove(i);
		user.setBorrowedBooks(currentBorrowedList);
	}

	@Override
	public ReturnStatus returnBothBooks(User user) {
		List<Book> currentBorrowedList = user.getBorrowedBooks();
		if (currentBorrowedList.size() == 0) {
			return ReturnStatus.NO_BOOKS_TO_RETURN;
		}
		for (Book book : currentBorrowedList) {
			addBookToRepo(book.getID(), user);
		}
		emptyBorrowList(user);
		return ReturnStatus.RETURN_SUCCESSFUL;
	}

	private void emptyBorrowList(User user) {
		user.setBorrowedBooks(new LinkedList<Book>());
	}
}
