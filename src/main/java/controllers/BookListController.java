package controllers;

import java.util.List;

import exceptions.bookListNotFoundException;
import models.Book;
import services.BookListService;

public class BookListController {
	// Dependency inversion used so that controller can work with any service
	// implementation based on the type of business rules to be used
	private final BookListService bookListService;

	public BookListController(BookListService bookListService) {
		this.bookListService = bookListService;
	}
	
	public void displayBooksInLibrary() {
		List<Book> booksInLibrary;
		try {
			booksInLibrary = bookListService.provideAllBooks();
			if(booksInLibrary==null)
				System.out.println("The Library is empty");
			else
			{
				displayAllBooksFromList(booksInLibrary);
				
			}
		} catch (bookListNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private void displayAllBooksFromList(List<Book> booksInLibrary) {
		System.out.println("The books presently in the Library are:");
		for(Book book:booksInLibrary) {
			System.out.println(book.getID()+" "+book.getName()+" "+book.getAuthor());
		}
		
	}
}
