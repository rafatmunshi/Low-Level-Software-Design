package dao;

import java.util.LinkedList;
import java.util.List;

import models.Book;

// This is implementation of the DAO to return data created in memory
public class BookDAOInMemImpl implements BookDAO {

	@Override
	public List<Book> getAllBooks() {
		List<Book> bookList = new LinkedList<Book>();
		bookList.add(new Book(1, "Name1", "Author1"));
		bookList.add(new Book(2, "Name2", "Author2"));
		bookList.add(new Book(3, "Name3", "Author3"));
		bookList.add(new Book(4, "Name4", "Author4"));
		bookList.add(new Book(5, "Name5", "Author5"));
		return bookList;
	}

}
