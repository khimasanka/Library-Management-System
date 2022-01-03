package controller;

import controller.impl.BookCaseService;
import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.BookCase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCaseController implements BookCaseService {

    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    @Override
    public boolean saveBookCase(BookCase bc) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("INSERT INTO bookCase VALUES (?,?,?)");
        stm.setObject(1,bc.getCaseId());
        stm.setObject(2, bc.getBookType());
        stm.setObject(3, bc.getBookLanguage());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean updateCase(BookCase bc) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE bookcase SET bookType=? ,bookLanguage=?  WHERE bookCaseNumber=?");
        stm.setObject(1,bc.getBookType());
        stm.setObject(2,bc.getBookLanguage());
        stm.setObject(3,bc.getCaseId());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteCase(String id) throws SQLException, ClassNotFoundException {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete.?",yes,no);
        alert.setTitle("Delete Member");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM bookcase WHERE bookCaseNumber='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;
    }

    public static List<BookCase> searchBookCase(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm= DbConnection.getInstance().getConnection().
                prepareStatement("SELECT * FROM bookcase WHERE bookcase.bookCaseNumber LIKE '" + id + "%' OR bookcase.bookType LIKE '" + id + "%'");
        ResultSet rst = stm.executeQuery();

        List<BookCase> books = new ArrayList<>();

        while (rst.next()) {
            books.add(new BookCase(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)

            ));
        }

        return books;
    }

    @Override
    public ArrayList<BookCase> getAllItems() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM bookCase");
        ResultSet rst = stm.executeQuery();
        ArrayList<BookCase>bookCases =new ArrayList<>();
        while (rst.next()){
            bookCases.add(new BookCase(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            ));
        }
        return bookCases;
    }

    public static String getBookCaseID() throws SQLException, ClassNotFoundException {
        ResultSet rs= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM bookCase ORDER BY bookCaseNumber DESC LIMIT 1"

        ).executeQuery();
        if (rs.next()) {
            int tempId = Integer.parseInt(rs.getString(1).split("-")[1]);
            tempId = tempId+1;
            if(tempId<=9){
                return "BC-0000"+tempId;
            }else if(tempId<=99){
                return "BC-000"+tempId;
            }else if(tempId<=999) {
                return "BC-00" + tempId;
            }else if(tempId<=9999){
                return "BC-0" + tempId;
            }else{
                return "BC-"+tempId;
            }
        }else{
            return "BC-00001";
        }
    }
}
