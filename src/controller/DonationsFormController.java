package controller;

import com.jfoenix.controls.JFXButton;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.*;
import util.ValidationUtil;
import view.tm.DonateTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class DonationsFormController {
    public JFXButton btnAddList;
    public TextField txtPersonName;
    public TextField txtQty;
    public TextField txtBookName;
    public Text lblDonateId;
    public Label lblDate;
    public TextField txtDonateSearch;
    public JFXButton btnCloseDetails;

    public TableView<DonateTM> tblDonateDetails;
    public TableColumn colPersonDetailName;
    public TableColumn colBookNameDetail;
    public TableColumn colQtyDetail;
    public TableColumn colDateDetail;
    public AnchorPane contextDonateDetails;
    public TableColumn colDonateIdDetails;
    public AnchorPane contextForTbl;
    public TableView<DonateTM> tblDonateCart;
    public TableColumn colDonateIdCart;
    public TableColumn colPersonNameCart;
    public TableColumn colBookNameCart;
    public TableColumn colDateCart;
    public TableColumn colQtyCart;
    public Label lblTblTitle;
    public JFXButton btnAddDonate;
    public JFXButton btnRemove;
    public JFXButton btnCartFieldRemove;

    int borrowSelectedRowForRemove = -1;

    ObservableList<DonateTM> obList = FXCollections.observableArrayList();
    ObservableList<DonateTM> detailList = FXCollections.observableArrayList();

    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    public void initialize() throws SQLException, ClassNotFoundException {
        setDonateID();
        setTableData();
        setDate();
        setTableCartData();
        tblDonateDetails.getItems().setAll(loadAllDonations());
        btnAddDonate.setDisable(true);
        btnRemove.setVisible(false);
        btnCartFieldRemove.setDisable(true);

        tblDonateCart.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            borrowSelectedRowForRemove = (int) newValue;
            if(newValue.equals(-1)){
                btnCartFieldRemove.setDisable(true);
            }else{
                btnCartFieldRemove.setDisable(false);
            }
        });

    }

    void setDate(){
        Date date = new Date();
        SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        donateValidations();
        btnAddList.setDisable(true);
    }

    public void setDonateID(){
        try {
            lblDonateId.setText(DonationController.getDonateId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<DonateTM> loadAllDonations() throws SQLException, ClassNotFoundException {
        ArrayList<DonateDetails> allMembers = DonationController.getAllDonations();

        detailList = FXCollections.observableArrayList();
        for (DonateDetails donate: allMembers) {
            detailList.add(new DonateTM(
                    donate.getDonateId(),
                    donate.getName(),
                    donate.getBookName(),
                    donate.getDate(),
                    donate.getQty()
            ));
        }
        return detailList;
    }

    void setTableData(){
        colDonateIdDetails.setCellValueFactory(new PropertyValueFactory<>("donateID"));
        colPersonDetailName.setCellValueFactory(new PropertyValueFactory<>("personName"));
        colBookNameDetail.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colDateDetail.setCellValueFactory(new PropertyValueFactory<>("date"));
        colQtyDetail.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    public void removeDonations(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = tblDonateDetails.getSelectionModel().getSelectedItem().getDonateID();
        boolean isDeleted =DonationController.deleteDonate(id);
        if(DonationController.deleteDonate(lblDonateId.getText())|| isDeleted){
            tblDonateDetails.getItems().setAll(loadAllDonations());
            
        }else {
        }
    }

    public void addToListOnAction(ActionEvent actionEvent) {
        String personName = txtPersonName.getText();
        String bookName = txtBookName.getText();
        String date = lblDate.getText();
        int qty = Integer.parseInt(txtQty.getText());

        DonateTM tm = new DonateTM(
                lblDonateId.getText(),
                personName,
                bookName,
                date,
                qty
        );
        int rowNumber= isExists(tm);
        if(isExists(tm)==-1){
            obList.add(tm);
        }else{
            DonateTM temp = obList.get(rowNumber);
            DonateTM newTm = new DonateTM(
                    temp.getDonateID(),
                    temp.getPersonName(),
                    temp.getBookName(),
                    temp.getDate(),
                    temp.getQty()+qty
            );
            obList.remove(rowNumber);
            obList.add(newTm);
        }
        tblDonateCart.setItems(obList);
        btnAddDonate.setDisable(false);
        contextForTbl.setVisible(true);
        lblTblTitle.setText("Donate Cart");
        btnCartFieldRemove.setVisible(true);
        txtPersonName.setDisable(true);
    }

    private int isExists(DonateTM tm){
        for (int i = 0; i < obList.size(); i++) {
            if(tm.getBookName().equals(obList.get(i).getBookName())){
                return i;
            }
        }
        return -1;
    }

    void setTableCartData(){
        colDonateIdCart.setCellValueFactory(new PropertyValueFactory<>("donateID"));
        colPersonNameCart.setCellValueFactory(new PropertyValueFactory<>("personName"));
        colBookNameCart.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colDateCart.setCellValueFactory(new PropertyValueFactory<>("date"));
        colQtyCart.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    public void saveDonateDetailsOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ArrayList<DonateBooks> books = new ArrayList<>();

        for (DonateTM temTm: obList
        ) {
            books.add( new DonateBooks(
                    temTm.getDonateID(),
                    temTm.getBookName(),
                    temTm.getQty()
            ));
        }
        Donate donate = new Donate(
                lblDonateId.getText(),
                txtPersonName.getText(),
                lblDate.getText(),
                books
        );
        if (DonationController.donateSave(donate)){
            new Alert(Alert.AlertType.CONFIRMATION,"Success").show();
            tblDonateDetails.getItems().setAll(loadAllDonations());
            btnAddDonate.setDisable(true);
            setDonateID();
            txtBookName.clear();
            txtPersonName.clear();
            txtQty.clear();
            txtDonateSearch.clear();
            obList.clear();
            txtPersonName.setDisable(false);
            btnAddList.setDisable(true);
        }else {
            new Alert(Alert.AlertType.WARNING,"Try Again").show();
        }
    }

    public void donationsSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        DonateDetails m1 = DonationController.searchDonate(txtDonateSearch.getText());
        if (m1 == null) {
            new Alert(Alert.AlertType.WARNING,"Empty Result Set..").show();
        }else{
            setSearchData(m1);
            btnAddList.setDisable(true);
            btnRemove.setDisable(false);
        }
    }

    void setSearchData(DonateDetails m){
        lblDonateId.setText(m.getDonateId());
        txtBookName.setText(m.getBookName());
        txtPersonName.setText(m.getName());
        txtQty.setText(String.valueOf(m.getQty()));

    }

    public void detailsTblOnAction(ActionEvent actionEvent) {
        contextForTbl.setVisible(false);
        lblTblTitle.setText("Donate Details");
        btnCartFieldRemove.setVisible(false);
        btnRemove.setVisible(true);
    }

    public void cartTblOnAction(ActionEvent actionEvent) {
        contextForTbl.setVisible(true);
        lblTblTitle.setText("Donate Cart");
        btnCartFieldRemove.setVisible(true);
        txtPersonName.setDisable(false);
        btnRemove.setVisible(false);
    }

    public void removeCartDonations(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure You Want to Delete.", yes, no);
        alert.setTitle("Delete Row");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(no) == yes) {
            if (borrowSelectedRowForRemove == -1) {

            } else {
                btnCartFieldRemove.setDisable(false);
                obList.remove(borrowSelectedRowForRemove);
                tblDonateCart.refresh();
                txtPersonName.setDisable(false );
            }
        } else {
        }
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void donateValidations(){
        map.put(txtPersonName,Pattern.compile("^[A-Za-z][A-Za-z\\'\\-.]+([\\ A-Za-z][A-Za-z\\'\\-.]+)*"));
        map.put(txtBookName,Pattern.compile("^[A-z\\d][\\dA-z:\\/\"'.]+([\\ A-z][A-z\\d\\:@#/]+)*$"));
        map.put(txtQty,Pattern.compile("^[0-9]{1,3}$"));
    }

    public void donateFieldFocus(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(map,btnAddList);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }
}



