package dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.Book;
import models.BookCopies;
import models.BorrowStatus;
import models.User;

// This is implementation of the DAO to return data created in memory
public class BookDAOInMemImpl implements BookDAO {
	static Map<Long, BookCopies> bookRepo= new HashMap<Long, BookCopies>();
	public BookDAOInMemImpl() {
		bookRepo.put((long) 1, new BookCopies(1, "Name1", "Author1", 2));
		bookRepo.put((long) 2, new BookCopies(2, "Name2", "Author2", 3));
		bookRepo.put((long) 3, new BookCopies(3, "Name3", "Author3", 0));
		bookRepo.put((long) 4, new BookCopies(4, "Name4", "Author4", 1));
		bookRepo.put((long) 5, new BookCopies(5, "Name5", "Author5", 4));
	}
	@Override
	public List<Book> getAllBooks() {
		List<Book> bookList = new LinkedList<Book>();
		for(Book book:bookRepo.values())
			bookList.add(book);
		return bookList;
	}

	@Override
	public Integer getCopiesOfBook(long bookId) {
		int copies=bookRepo.get(bookId).getCopiesOfBook();
		return copies;
	}
	@Override
	public Boolean doesBookExist(long bookId) {
		return(bookRepo.containsKey(bookId));
	}
	@Override
	public BookCopies getBook(long bookId) {
		return bookRepo.get(bookId);
	}
	@Override
	public BookCopies removeBook(long bookId) {
		return bookRepo.remove(bookId);
	}

}
