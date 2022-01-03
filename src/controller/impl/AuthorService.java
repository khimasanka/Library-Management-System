package controller.impl;

import model.Authors;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AuthorService {
    public boolean saveAuthor(Authors n) throws SQLException, ClassNotFoundException;
    public boolean updateAuthor(Authors n) throws SQLException, ClassNotFoundException;
    public boolean deleteAuthor(String id) throws SQLException, ClassNotFoundException;
    //public Authors searchAuthor(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<Authors> getAllAuthors( ) throws SQLException, ClassNotFoundException;
}
