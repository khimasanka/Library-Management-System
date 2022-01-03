package controller;

import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Member;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SettingsController {

    public static Users getUser(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm= DbConnection.getInstance().getConnection().
                prepareStatement("SELECT * FROM users WHERE userName=?");
        stm.setObject(1,id);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            return new Users(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5),
                    rst.getString(6),
                    rst.getString(7)
            );

        }else{
            return null;
        }
    }

    public static boolean changeUserDetails(Users u) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE users SET userFullName=? ,userName=? ,userPassword=? , userMobile=? , userAddress=? , role=? WHERE userId=?");
        stm.setObject(1,u.getUserFullName());
        stm.setObject(2,u.getUserName());
        stm.setObject(3,u.getPassword());
        stm.setObject(4,u.getMobile());
        stm.setObject(5,u.getUserAddress());
        stm.setObject(6,u.getRole());
        stm.setObject(7,u.getUserid());
        return stm.executeUpdate()>0;
    }

    public static boolean deleteUserAccount(String id) throws SQLException, ClassNotFoundException {

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete Account.?",yes,no);
        alert.setTitle("Delete Account");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM users WHERE userId='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;
    }

}
