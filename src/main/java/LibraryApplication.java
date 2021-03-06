import java.util.LinkedList;
import java.util.Scanner;

import controllers.BookController;
import dao.BookDAOInMemImpl;
import models.Book;
import models.User;
import services.BookServiceImpl;

public class LibraryApplication {
	static final int EXIT = 20;
	static final BookController forThisSpecificImpl = new BookController(new BookServiceImpl(new BookDAOInMemImpl()));
	static final User currentUser = new User(1, new LinkedList<Book>());

	public static void main(String[] args) {

		System.out.println("Welcome to the Library Management System");
		while (true) {
			giveOptionsToUser();
			int choice = takeChoiceFromUser();
			boolean isExitChoice = choice == EXIT ? true : false;
			if (isExitChoice)
				break;
			else {
				executeChoice(choice);
			}

		}
	}

	private static void executeChoice(int choice) {
		switch (choice) {
		case 1:
			displayBooksInLibrary();
			break;
		case 2:
			borrowBook();
			break;
		case 3:
			returnABook();
			break;
		case 4:
			returnBothBooks();
			break;
		default:
			System.out.println("Invalid choice");
		}
	}

	private static void returnBothBooks() {
		forThisSpecificImpl.returnBothBooks(currentUser);
	}

	static void returnABook() {
		System.out.println("Which book do you want to return? Mention its ID");
		long bookId = takeChoiceFromUser();
		forThisSpecificImpl.returnABook(bookId, currentUser);
	}

	static void borrowBook() {
		System.out.println("Which book do you want to borrow? Mention its ID");
		long bookId = takeChoiceFromUser();
		forThisSpecificImpl.borrowBook(bookId, currentUser);
	}

	private static void displayBooksInLibrary() {
		forThisSpecificImpl.displayBooksInLibrary();
	}

	static int takeChoiceFromUser() {
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		return choice;
	}

	static void giveOptionsToUser() {
		System.out.println("Choose an option-");
		System.out.println("1. View Books in the Library");
		System.out.println("2. Borrow a book");
		System.out.println("3. Return a book");
		System.out.println("4. Return both books");
		System.out.println("20. Exit");
	}
}
