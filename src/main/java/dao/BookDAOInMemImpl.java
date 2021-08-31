package dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.Book;
import models.BorrowStatus;
import models.User;

// This is implementation of the DAO to return data created in memory
public class BookDAOInMemImpl implements BookDAO {
	static Map<Long, Book> bookRepo= new HashMap<Long, Book>();
	public BookDAOInMemImpl() {
		bookRepo.put((long) 1, new Book(1, "Name1", "Author1"));
		bookRepo.put((long) 2, new Book(2, "Name2", "Author2"));
		bookRepo.put((long) 3, new Book(3, "Name3", "Author3"));
		bookRepo.put((long) 4, new Book(4, "Name4", "Author4"));
		bookRepo.put((long) 5, new Book(5, "Name5", "Author5"));
	}
	@Override
	public List<Book> getAllBooks() {
		List<Book> bookList = new LinkedList<Book>();
		for(Book book:bookRepo.values())
			bookList.add(book);
		return bookList;
	}
	@Override
	public BorrowStatus borrowBook(long bookId, User user) {
		if(bookRepo.containsKey(bookId)) {
			List<Book> currentBorrowedList= user.getBorrowedBooks();
			currentBorrowedList.add(bookRepo.get(bookId));
			user.setBorrowedBooks(currentBorrowedList);
			bookRepo.remove(bookId);
			return BorrowStatus.SUCCESS;
		}
		else
			return BorrowStatus.INVALID_BOOK_ID;
	}

}
