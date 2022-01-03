package controller;

import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Donate;
import model.DonateBooks;
import model.DonateDetails;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class DonationController {

    public static boolean donateSave(Donate donate){
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                    "INSERT INTO donate VALUES (?,?,?)");

            stm.setObject(1,donate.getDonateId());
            stm.setObject(2,donate.getName());
            stm.setObject(3,donate.getDate());

            if(stm.executeUpdate()>0){
                return saveDonateDetail(donate.getDonateId(),donate.getBooks());
            }else{
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean saveDonateDetail(String donateId, ArrayList<DonateBooks> books) throws SQLException, ClassNotFoundException {
        for (DonateBooks temp: books
        ) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                    "INSERT INTO donatedbooks VALUES (?,?,?)");
            stm.setObject(1,temp.getDonateId());
            stm.setObject(2,temp.getBookName());
            stm.setObject(3,temp.getQty());

            if(stm.executeUpdate()>0){
            }else {
                return false;
            }
        }
        return true;
    }

    public static DonateDetails searchDonate(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm= DbConnection.getInstance().getConnection().
                prepareStatement("SELECT d.donateId,d.personName , db.bookName ,db.qty FROM donate AS d INNER JOIN donatedbooks AS db ON d.donateId  = db.donateId where d.donateId=?");
        stm.setObject(1,id);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            return new DonateDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4)
            );

        }else{
            return null;
        }
    }

    public static boolean deleteDonate(String id) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete.?",yes,no);
        alert.setTitle("Delete Member");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM donate WHERE donateId='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;
    }

    public static ArrayList<DonateDetails> getAllDonations() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "select d.donateId,d.personName ,db.bookName ,d.donateDate,db.qty  From donate as d inner " +
                        "join donatedbooks as db on d.donateId = db.donateId;");
        ResultSet rst = stm.executeQuery();
        ArrayList<DonateDetails>books =new ArrayList<>();
        while (rst.next()){
            books.add(new DonateDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5)

            ));
        }
        return books;
    }

    public static String getDonateId() throws SQLException, ClassNotFoundException {
        ResultSet rs= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM donate ORDER BY donateId DESC LIMIT 1"

        ).executeQuery();
        if (rs.next()) {
            int tempId = Integer.parseInt(rs.getString(1).split("-")[1]);
            tempId = tempId+1;
            if(tempId<=9){
                return "D-0000"+tempId;
            }else if(tempId<=99){
                return "D-000"+tempId;
            }else if(tempId<=999){
                return "D-00"+tempId;
            }else if(tempId<=9999){
                return "D-0"+tempId;
            }else{
                return "D-"+tempId;
            }
        }else{
            return "D-00001";
        }
    }


}
