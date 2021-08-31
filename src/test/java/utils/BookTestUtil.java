package utils;

import java.util.LinkedList;
import java.util.List;

import models.Book;
import models.User;

public class BookTestUtil {
	public static List<Book> getAllBooksUtil() {
		List<Book> bookList = new LinkedList<Book>();
		bookList.add(new Book(1, "Name1", "Author1"));
		bookList.add(new Book(2, "Name2", "Author2"));
		bookList.add(new Book(3, "Name3", "Author3"));
		bookList.add(new Book(4, "Name4", "Author4"));
		bookList.add(new Book(5, "Name5", "Author5"));
		return bookList;
	}

	public static List<Book> getBorrowedBooksTillLimitUtil() {
		int borrowLimit=User.getBorrowLimit();
		List<Book> bookList = new LinkedList<Book>();
		for(int i=1; i<=borrowLimit;i++) {
			bookList.add(new Book(i, "Name"+i, "Author"+i));
		}
		return bookList;
	}
}
