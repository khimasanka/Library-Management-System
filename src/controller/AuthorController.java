package controller;

import controller.impl.AuthorService;
import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Authors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorController implements AuthorService {

    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    @Override
    public boolean saveAuthor(Authors n) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO author VALUES (?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,n.getAuthorId());
        stm.setObject(2, n.getName());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean updateAuthor(Authors n) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE author SET authorName=? WHERE authorId=?");
        stm.setObject(1,n.getName());
        stm.setObject(2,n.getAuthorId());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteAuthor(String id) throws SQLException, ClassNotFoundException {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete.?",yes,no);
        alert.setTitle("Delete Member");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM author WHERE authorId='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;

    }

    public static List<Authors> searchAuthor(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM author WHERE author.authorId LIKE '" + value + "%' OR author.authorName LIKE '" + value + "%' ");
        ResultSet rs = pstm.executeQuery();

        List<Authors> authors = new ArrayList<>();

        while (rs.next()) {
            authors.add(new Authors(
                    rs.getString("authorId"),
                    rs.getString("authorName")
            ));
        }

        return authors;
    }

    @Override
    public ArrayList<Authors> getAllAuthors() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM author");
        ResultSet rst = stm.executeQuery();
        ArrayList<Authors>authors =new ArrayList<>();
        while (rst.next()){
            authors.add(new Authors(
                    rst.getString(1),
                    rst.getString(2)

            ));
        }
        return authors;
    }

    public static String getAuthorId() throws SQLException, ClassNotFoundException {
        ResultSet rs= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM author ORDER BY authorId DESC LIMIT 1"

        ).executeQuery();
        if (rs.next()) {
            int tempId = Integer.parseInt(rs.getString(1).split("-")[1]);
            tempId = tempId+1;
            if(tempId<=9){
                return "A-0000"+tempId;
            }else if(tempId<=99){
                return "A-000"+tempId;
            }else if(tempId<=999){
                return "A-00"+tempId;
            }else if(tempId<=9999){
                return "A-0"+tempId;
            }else{
                return "A-"+tempId;
            }
        }else{
            return "A-00001";
        }
    }
}
