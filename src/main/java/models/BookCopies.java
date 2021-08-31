package models;

public class BookCopies extends Book{
Integer copiesOfBook;

public Integer getCopiesOfBook() {
	return copiesOfBook;
}
public void setCopiesOfBook(Integer copiesOfBook) {
	this.copiesOfBook = copiesOfBook;
}
public BookCopies(long iD, String name, String author, Integer copiesOfBook) {
	super(iD, name, author);
	this.copiesOfBook = copiesOfBook;
}

}
