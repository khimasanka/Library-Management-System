package controller;

import controller.impl.BooksService;
import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BooksController implements BooksService {

    @Override
    public boolean saveBooks(Books b) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("INSERT INTO book VALUES (?,?,?,?,?,?)");
        stm.setObject(1,b.getId());
        stm.setObject(2, b.getBookName());
        stm.setObject(3, b.getBookType());
        stm.setObject(4, b.getLanguage());
        stm.setObject(5, b.getAuthorName());
        stm.setObject(6, b.getCount());

        return stm.executeUpdate()>0;
    }

    @Override
    public boolean updateBooks(Books b) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE book SET bookName=? ,bookType=? ,language=?, authorName=? ,count=? WHERE bookId=?");
        stm.setObject(1,b.getBookName());
        stm.setObject(2,b.getBookType());
        stm.setObject(3,b.getLanguage());
        stm.setObject(4,b.getAuthorName());
        stm.setObject(5,b.getCount());
        stm.setObject(6,b.getId());
        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteBooks(String id) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete..?",yes,no);
        alert.setTitle("Delete Book");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM book WHERE bookId='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;
    }

    @Override
    public Books searchBooks(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm= DbConnection.getInstance().getConnection().
                prepareStatement("SELECT * FROM book WHERE bookId=?");
        stm.setObject(1,id);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            return new Books(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getInt(6)
            );

        }else{
            return null;
        }
    }

    @Override
    public ArrayList<Books> getAllBooks() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM book");
        ResultSet rst = stm.executeQuery();
        ArrayList<Books>books =new ArrayList<>();
        while (rst.next()){
            books.add(new Books(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getInt(6)
            ));
        }
        return books;
    }

    public static List<Books> searchBookForAll(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM book WHERE book.bookId LIKE '" + value + "%' OR bookName LIKE '" + value + "%' OR book.authorName LIKE '" + value + "%'");
        ResultSet rs = pstm.executeQuery();

        List<Books> books = new ArrayList<>();

        while (rs.next()) {
            books.add(new Books(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getInt(6)
            ));
        }

        return books;
    }

    public static String getBookId() throws SQLException, ClassNotFoundException {
        ResultSet rs= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM book ORDER BY bookId DESC LIMIT 1"

        ).executeQuery();
        if (rs.next()) {
            int tempId = Integer.parseInt(rs.getString(1).split("-")[1]);
            tempId = tempId+1;
            if(tempId<=9){
                return "B-0000"+tempId;
            }else if(tempId<=99){
                return "B-000"+tempId;
            }else if(tempId<=999){
                return "B-00"+tempId;
            }else if(tempId<=9999){
                return "B-0"+tempId;
            }else{
                return "B-"+tempId;
            }
        }else{
            return "B-00001";
        }
    }

}
