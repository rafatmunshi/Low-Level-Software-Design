import java.util.Scanner;

import controllers.BookListController;
import dao.BookDAOInMemImpl;
import services.BooksListServiceImpl;

public class LibraryApplication {
	public static void main(String[] args) {
		System.out.println("Welcome to the Library Management System");
		while (true) {
			giveOptionsToUser();
			int choice = takeChoiceFromUser();
			boolean isExitChoice = choice == 2 ? true : false;
			if (isExitChoice)
				break;
			else
				executeChoice(choice);
		}
	}

	private static void executeChoice(int choice) {
		switch (choice) {
		case 1:
			displayBooksInLibrary();
			break;
		default:
			System.out.println("Invalid choice");
		}
	}

	private static void displayBooksInLibrary() {
		BookListController forThisSpecificImpl = new BookListController(new BooksListServiceImpl(new BookDAOInMemImpl()));
		forThisSpecificImpl.displayBooksInLibrary();
	}

	private static int takeChoiceFromUser() {
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		return choice;
	}

	private static void giveOptionsToUser() {
		System.out.println("Choose an option-");
		System.out.println("1. View Books in the Library");
		System.out.println("2. Exit");
	}
}
