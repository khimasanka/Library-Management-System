package controller.impl;

import model.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemService {
    public boolean saveItem(Item i) throws SQLException, ClassNotFoundException;
    public boolean updateItem(Item i) throws SQLException, ClassNotFoundException;
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    public Item searchItem(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<Item> getAllItems( ) throws SQLException, ClassNotFoundException;
}
