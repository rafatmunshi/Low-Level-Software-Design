package services;

import java.util.List;

import dao.BookDAO;
import exceptions.bookListNotFoundException;
import models.Book;

public class BooksListServiceImpl implements BookListService{

	private final BookDAO bookDAO;

	public BooksListServiceImpl(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	public List<Book> provideAllBooks() throws bookListNotFoundException{
		List<Book> booksList= bookDAO.getAllBooks();
		if(booksList==null)
			throw new bookListNotFoundException("The books list could not be retrieved at the moment");
		else
			if(booksList.isEmpty())
				return null;
			else
				return booksList;
	}
}
