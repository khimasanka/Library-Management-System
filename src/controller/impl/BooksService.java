package controller.impl;

import model.Books;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BooksService {
    public boolean saveBooks(Books b) throws SQLException, ClassNotFoundException;
    public boolean updateBooks(Books b) throws SQLException, ClassNotFoundException;
    public boolean deleteBooks(String id) throws SQLException, ClassNotFoundException;
    public Books searchBooks(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<Books> getAllBooks( ) throws SQLException, ClassNotFoundException;
}
