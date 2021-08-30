package models;

public class Book {
long ID;
String name;
String author;
public long getID() {
	return ID;
}
public void setID(long iD) {
	ID = iD;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public Book(long iD, String name, String author) {
	super();
	ID = iD;
	this.name = name;
	this.author = author;
}

}
