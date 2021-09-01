package controllers;

import java.util.List;

import exceptions.BookListNotFoundException;
import models.Book;
import models.BookCopies;
import models.BorrowStatus;
import models.ReturnStatus;
import models.User;
import services.BookService;

public class BookController {
	protected final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	public void displayBooksInLibrary() {
		List<Book> booksInLibrary;
		try {
			booksInLibrary = bookService.provideAllBooks();
			if (booksInLibrary == null)
				System.out.println("The Library is empty");
			else {
				displayAllBooksFromList(booksInLibrary);
			}
		} catch (BookListNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void displayAllBooksFromList(List<Book> booksInLibrary) {
		System.out.println("The books presently in the Library are:");
		for (Book book : booksInLibrary) {
			System.out.println(book.getID() + " " + book.getName() + " " + book.getAuthor());
		}

	}

	public void borrowBook(long bookId, User user) {
		BorrowStatus borrowStatus = bookService.borrowBook(bookId, user);
		System.out.println(borrowStatus.toString());
	}

	public void returnABook(long bookId, User user) {
		ReturnStatus returnStatus = bookService.returnABook(bookId, user);
		System.out.println(returnStatus.toString());
	}

	public void returnBothBooks(User user) {
		ReturnStatus returnStatus = bookService.returnBothBooks(user);
		System.out.println(returnStatus.toString());
	}

}
