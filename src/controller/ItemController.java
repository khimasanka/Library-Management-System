package controller;

import controller.impl.ItemService;
import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Books;
import model.Item;
import model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ItemController implements ItemService {
    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
    @Override
    public boolean saveItem(Item i) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("INSERT INTO item VALUES (?,?,?,?)");
        stm.setObject(1,i.getItemId());
        stm.setObject(2, i.getItemName());
        stm.setObject(3,i.getItemType());
        stm.setObject(4, i.getQty());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean updateItem(Item i) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE item SET itemName=? ,itemType=? ,qty=? WHERE itemId=?");
        stm.setObject(1,i.getItemName());
        stm.setObject(2,i.getItemType());
        stm.setObject(3,i.getQty());
        stm.setObject(4,i.getItemId());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete.?",yes,no);
        alert.setTitle("Delete Member");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM item WHERE itemId='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;

    }

    @Override
    public Item searchItem(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT * FROM item WHERE itemId=?");
        stm.setObject(1, id);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4)
            );

        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM item");
        ResultSet rst = stm.executeQuery();
        ArrayList<Item> items =new ArrayList<>();
        while (rst.next()){
            items.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4)
            ));
        }
        return items;
    }


    public static String getItemId() throws SQLException, ClassNotFoundException {
        ResultSet rs= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM item ORDER BY itemId DESC LIMIT 1"

        ).executeQuery();
        if (rs.next()) {
            int tempId = Integer.parseInt(rs.getString(1).split("-")[1]);
            tempId = tempId+1;
            if(tempId<=9){
                return "I-000"+tempId;
            }else if(tempId<=99){
                return "I-00"+tempId;
            }else if(tempId<=999){
                return "I-0"+tempId;
            }else{
                return "I-"+tempId;
            }
        }else{
            return "I-0001";
        }
    }

}
