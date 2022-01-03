package controller.impl;

import model.BookCase;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BookCaseService {
    public boolean saveBookCase(BookCase bc) throws SQLException, ClassNotFoundException;
    public boolean updateCase(BookCase bc) throws SQLException, ClassNotFoundException;
    public boolean deleteCase(String id) throws SQLException, ClassNotFoundException;
    //public BookCase searchCase(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<BookCase> getAllItems( ) throws SQLException, ClassNotFoundException;}
