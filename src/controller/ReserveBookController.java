package controller;

import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.ReserveBook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReserveBookController {


    public static boolean reserveDateUpdate(ReserveBook m) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                " UPDATE borrow SET reserveDate =? WHERE borrowId=?");
        stm.setObject(1,m.getReserveDate());
        stm.setObject(2,m.getBorrowId());
        return stm.executeUpdate()>0;
    }

    public static boolean deleteBorrowDetail(String id) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete.?",yes,no);
        alert.setTitle("Delete Member");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM borrow WHERE borrowId='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;
    }

    public static ArrayList<ReserveBook> getAllBorrows() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "select b.borrowId,b.memberId ,b.memName ,bD.bookId, b.borrowDate ,b.reserveDate  From borrow as b inner " +
                        "join borrowDetails as bD on b.borrowId = bD.borrowId;");
        ResultSet rst = stm.executeQuery();
        ArrayList<ReserveBook>books =new ArrayList<>();
        while (rst.next()){
            books.add(new ReserveBook(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return books;
    }

    public static ReserveBook searchBorrow(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm= DbConnection.getInstance().getConnection().
                prepareStatement("SELECT b.borrowId,b.memberId , b.memName ,bd.bookId, bd.bookName FROM borrow AS b INNER JOIN borrowDetails AS bD ON b.borrowId  = bD.borrowid where b.borrowId=?");
        stm.setObject(1,id);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            return new ReserveBook(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );

        }else{
            return null;
        }
    }

    public static void updateQty(String bookId) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE book SET count = count+1 WHERE bookId='b-00002'"
        );
        stm.executeUpdate();
    }

}
