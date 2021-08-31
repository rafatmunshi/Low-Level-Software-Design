package dao;

import java.util.List;

import models.Book;
import models.BorrowStatus;
import models.User;

public interface BookDAO {
List<Book> getAllBooks();

BorrowStatus borrowBook(long bookId, User user);
}
