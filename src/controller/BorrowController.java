package controller;

import db.DbConnection;
import model.Borrow;
import model.BorrowBooksDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BorrowController {
    public static boolean saveBorrow(Borrow borrow){
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement(
                    "INSERT INTO borrow VALUES (?,?,?,?,?,?)");
            stm.setObject(1,borrow.getBorrowId());
            stm.setObject(2,borrow.getMemberId());
            stm.setObject(3,borrow.getMemName());
            stm.setObject(4,borrow.getBorrowDate());
            stm.setObject(5,borrow.getBorrowTime());
            stm.setObject(6,borrow.getReserveBook());

            if(stm.executeUpdate()>0){
                if( saveBookDetail(borrow.getBorrowId(),borrow.getBooks())){
                    con.commit();
                    return true;
                }else{
                    con.rollback();
                    return false;
                }
            }else{
                con.rollback();
                return false;
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public static String getBorrowId() throws SQLException, ClassNotFoundException {
        ResultSet rs= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM borrow ORDER BY borrowId DESC LIMIT 1"

        ).executeQuery();
        if (rs.next()) {
            int tempId = Integer.parseInt(rs.getString(1).split("-")[1]);
            tempId = tempId+1;
            if(tempId<=9){
                return "BN-0000"+tempId;
            }else if(tempId<=99){
                return "BN-000"+tempId;
            }else if(tempId<=999){
                return "BN-00"+tempId;
            }else if(tempId<=9999){
                return "BN-0"+tempId;
            }else{
                return "BN-"+tempId;
            }
        }else{
            return "BN-00001";
        }
    }

    private static boolean saveBookDetail(String borrowId, ArrayList<BorrowBooksDetail> books) throws SQLException, ClassNotFoundException {
        for (BorrowBooksDetail temp : books){
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                    "INSERT INTO borrowDetails VALUES (?,?,?,?)");
            stm.setObject(1,borrowId);
            stm.setObject(2,temp.getBookId());
            stm.setObject(3,temp.getBookName());
            stm.setObject(4,temp.getBorrowDate());

            if(stm.executeUpdate()>0){
                if(updateQty(temp.getBookId(),1)){

                }else {
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(String bookId, int qty) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE book SET count=(count-" + qty + ") WHERE bookId='" + bookId + "'"
        );
        return stm.executeUpdate()>0;
    }

}
