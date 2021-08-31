package models;

import java.util.List;

public class User {
	long ID;
	List<Book> borrowedBooks;
	static final int BORROW_LIMIT = 2;

	public long getID() {
		return ID;
	}

	public static int getBorrowLimit() {
		return BORROW_LIMIT;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public List<Book> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<Book> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	public User(long iD, List<Book> borrowedBooks) {
		super();
		ID = iD;
		this.borrowedBooks = borrowedBooks;
	}

}
