package services;

import java.util.List;

import exceptions.bookListNotFoundException;
import models.Book;

public interface BookListService {
	List<Book> provideAllBooks() throws bookListNotFoundException;
}
