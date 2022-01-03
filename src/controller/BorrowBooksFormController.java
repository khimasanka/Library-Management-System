package controller;

import com.jfoenix.controls.JFXButton;
import db.DbConnection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import model.Books;
import model.Borrow;
import model.BorrowBooksDetail;
import model.Member;
import util.ValidationUtil;
import view.tm.BooksTM;
import view.tm.BorrowTM;
import view.tm.MemberTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class BorrowBooksFormController {

    public TextField txtBookIdToBorrow;
    public TextField txtBookNameToBorrow;
    public TableView<BorrowTM> tblBorrow;
    public TableColumn colBookId;
    public TableColumn colBookName;
    public TableColumn colMemberId;
    public TableColumn colMemberName;
    public TableColumn colBorrowDate;
    public Text lblBorrowId;
    public TextField txtMemberIDToBorrow;
    public TextField txtMemberName;
    public TextField txtQtyOnStore;
    public Text lblDate;
    public Label lblTime;
    public JFXButton btnClearField;
    public JFXButton btnBorrowAddToList;
    public JFXButton btnSaveBorrow;
    public TextField txtSearchBooksToBorrow;

    int borrowSelectedRowForRemove = -1;
    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    ObservableList<BorrowTM> obList = FXCollections.observableArrayList();

    public void initialize(){
        setDate();
        setTableData();
        setBorrowId();
        btnClearField.setDisable(true);
        btnSaveBorrow.setDisable(true);

        borrowsValidate();
        btnBorrowAddToList.setDisable(true);

        txtSearchBooksToBorrow.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                bookSearchByName(oldValue);
            }
        });

        tblBorrow.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            borrowSelectedRowForRemove = (int) newValue;
            if(newValue.equals(-1)){
                btnClearField.setDisable(true);
            }else{
                btnClearField.setDisable(false);
            }
        });
    }

    public void searchBookToBorrow(ActionEvent actionEvent)  {
        Books b1 = null;
        try {
            b1 = new BooksController().searchBooks(txtBookIdToBorrow.getText());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (b1 == null) {
            new Alert(Alert.AlertType.WARNING,"Empty Result Set..").show();
        }else{
            txtBookNameToBorrow.setText(b1.getBookName());
            txtQtyOnStore.setText(String.valueOf(b1.getCount()));
        }
    }

    public void searchMemberToBorrow(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Member m1 = null;
            m1 = new MemberController().searchMember(txtMemberIDToBorrow.getText());
        if (m1 == null) {
            new Alert(Alert.AlertType.WARNING,"Empty Result Set..").show();
        }else{
            txtMemberName.setText(m1.getName());
        }
    }

    public void setBorrowId()  {
        try {
            lblBorrowId.setText(BorrowController.getBorrowId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setDate(){
        Date date = new Date();
        SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        final Thread thread = new Thread(()->{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm aa ");
            while (true){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                final String times=simpleDateFormat.format(new Date());
                Platform.runLater(()->{
                    lblTime.setText(times);
                });
            }
        });
        thread.start();

    }

    public void addToBorrowListOnAction(ActionEvent actionEvent) {
        String bookId = txtBookIdToBorrow.getText();
        String bookName = txtBookNameToBorrow.getText();
        String memberId = txtMemberIDToBorrow.getText();
        String memberName= txtMemberName.getText();
        String borrowDate = lblDate.getText();

        BorrowTM tm = new BorrowTM(
                bookId,bookName,memberId,memberName,borrowDate
        );
        if(isExists(tm) == -1){
            obList.add(tm);
            txtMemberIDToBorrow.setEditable(false);
            btnSaveBorrow.setDisable(false);
        }else {
            new Alert(Alert.AlertType.WARNING,"Choose one book type or one member at a time").show();
        }
        tblBorrow.setItems(obList);
    }

    private int isExists(BorrowTM tm){
        for (int i=0; i<obList.size(); i++){
            if (tm.getBookName().equals(obList.get(i).getBookName())) {
                return i;
            }
        }
        return -1;
    }

    public void setTableData(){
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    }

    public void borrowOnAction(ActionEvent actionEvent) {
        ArrayList<BorrowBooksDetail> books =new ArrayList<>();
        for (BorrowTM tempTm: obList){
            books.add(
                    new BorrowBooksDetail(
                            tempTm.getBookId(),
                            tempTm.getBookName(),
                            tempTm.getBorrowDate()
                    )
            );
        }
        Borrow borrow = new Borrow(
                lblBorrowId.getText(),
                txtMemberIDToBorrow.getText(),
                txtMemberName.getText(),
                lblDate.getText(),
                lblTime.getText(),
                "Borrowed",
                books
        );
        if(BorrowController.saveBorrow(borrow)){
            new Alert(Alert.AlertType.CONFIRMATION,"Success").show();
            setBorrowId();
            btnSaveBorrow.setDisable(true);
            btnBorrowAddToList.setDisable(true);
            clearMemberFields();
            obList.clear();
            txtMemberIDToBorrow.setEditable(true);
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again").show();
        }
    }

    void clearMemberFields(){
        txtMemberName.clear();
        txtBookIdToBorrow.clear();
        txtMemberIDToBorrow.clear();
        txtQtyOnStore.clear();
        txtBookNameToBorrow.clear();
    }

    public void removeTableField(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure You Want to Delete.", yes, no);
        alert.setTitle("Delete Row");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(no) == yes) {
            if (borrowSelectedRowForRemove == -1) {

            } else {
                btnClearField.setDisable(false);
                obList.remove(borrowSelectedRowForRemove);
                tblBorrow.refresh();
            }
        } else {
        }
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void borrowsValidate(){
        map.put(txtBookIdToBorrow,Pattern.compile("^(b-|B-)[0-9]{5}$"));
        map.put(txtBookNameToBorrow,Pattern.compile("^[A-z\\d][\\dA-z:\\/\"'.]+([\\ A-z][A-z\\d\\:@#/]+)*$"));
        map.put(txtQtyOnStore,Pattern.compile("^[0-9]{1,3}$"));
        map.put(txtMemberIDToBorrow,Pattern.compile("^(m-|M-)[0-9]{5}$"));
        map.put(txtMemberName,Pattern.compile("^[A-z][A-z.]+([\\ A-z][A-z.]+)*$"));
    }

    public void borrowFieldFocus(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(map,btnBorrowAddToList);
        if (keyEvent.getCode()== KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){

            }
        }
    }

    private void bookSearchByName(String value){
        try {
            List<Books> books = BooksController.searchBookForAll(value);
            for (Books book : books) {

                txtBookIdToBorrow.setText(book.getId());
                txtBookNameToBorrow.setText(book.getBookName());
                txtQtyOnStore.setText(String.valueOf(book.getCount()));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void searchBooksToBorrow(ActionEvent actionEvent) {
        bookSearchByName(txtSearchBooksToBorrow.getText());
    }
}
