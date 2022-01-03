package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.ReserveBook;
import util.ValidationUtil;
import view.tm.ReserveBookTm;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ReserveBooksFormController {
    public TextField txtBookIdToReserve;
    public TableView<ReserveBookTm> tblReserve;
    public TableColumn colMemberId;
    public TableColumn colMemberName;
    public TableColumn colBookId;
    public TableColumn colBorrowDate;
    public TextField txtBookNameToReserve;
    public JFXButton btnRemove;
    public TextField txtMemberIDToReserve;
    public TextField txtMemberName;
    public TextField txtBorrowIdToReserve;
    public TableColumn colBorrowId;
    public TableColumn colReserveDate;
    public Label lblDate;
    public JFXButton btnReserve;

    ObservableList<ReserveBookTm> obList;

    public void initialize() throws SQLException, ClassNotFoundException {
        loadAllReservesToTable();
        setDate();
        tblReserve.getItems().setAll(loadAllReserves());

        reserveValidation();
        btnReserve.setDisable(true);

    }

    public void setDate(){
        Date date= new Date();
        SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
    }

    public void reserveBook(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ReserveBook m1 = new ReserveBook(
                txtBorrowIdToReserve.getText(),
                lblDate.getText()
        );

        if(ReserveBookController.reserveDateUpdate(m1)){
             new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
             ReserveBookController.updateQty(m1.getBookId());
            tblReserve.getItems().setAll(loadAllReserves());
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void removeTableField(ActionEvent actionEvent) {
        String id = tblReserve.getSelectionModel().getSelectedItem().getBorrowId();
        boolean isDeleted = false;
        try {
            isDeleted = ReserveBookController.deleteBorrowDetail(id);

        if(isDeleted) {
            tblReserve.getItems().setAll(loadAllReserves());
        }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    public void searchMemberToBorrow(ActionEvent actionEvent) {
    }

    public void searchBorrowToReserve(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ReserveBook b1 = ReserveBookController.searchBorrow(txtBorrowIdToReserve.getText());
        if (b1 == null) {
            new Alert(Alert.AlertType.WARNING,"Empty Result Set..").show();
        }else{
            setSearchData(b1);
        }
    }

    void setSearchData(ReserveBook m){
        txtBookIdToReserve.setText(m.getBookId());
        txtBookNameToReserve.setText(m.getBookName());
        txtMemberName.setText(m.getMemberName());
        txtMemberIDToReserve.setText(m.getMemberId());
    }

    public void loadAllReservesToTable()  {
        colBorrowId.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colReserveDate.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));

    }

    public ObservableList<ReserveBookTm> loadAllReserves() throws SQLException, ClassNotFoundException {
        List<ReserveBook> allReserves = ReserveBookController.getAllBorrows();

        obList = FXCollections.observableArrayList();
        for (ReserveBook book: allReserves) {
            obList.add(new ReserveBookTm(
                    book.getBorrowId(),
                    book.getMemberId(),
                    book.getMemberName(),
                    book.getBookId(),
                    book.getBorrowDate(),
                    book.getReserveDate()
            ));
        }
        return obList;
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void reserveValidation(){
        map.put(txtBorrowIdToReserve,Pattern.compile("^(bn-|BN-|Bn-)[0-9]{5}$"));
        map.put(txtMemberIDToReserve,Pattern.compile("^(M-|m-)[0-9]{5}$"));
    }

    public void reserveFieldFocus(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(map,btnReserve);
        if (keyEvent.getCode()== KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){

            }
        }
    }
}
