package controller;

import com.jfoenix.controls.*;
import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CreateAccountFormController {
    public AnchorPane signUpContext;
    public TextField txtName;
    public TextField txtMobile;
    public TextField txtAddress;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public JFXButton btnCreateAccount;
    public JFXComboBox cmbRole;
    public CheckBox chkBox;
    public TextField txtPasswordView;


    LinkedHashMap<TextField , Pattern> map = new LinkedHashMap<>();
    Pattern namePattern =  Pattern.compile("^[A-Za-z][A-Za-z\\'\\-.]+([\\ A-Za-z][A-Za-z\\'\\-.]+)*");
    Pattern mobilePattern =  Pattern.compile("^(07)[0,1,2,4,5,6,7,8][-]?[0-9]{7}$");
    Pattern addressPattern =  Pattern.compile("^[A-z0-9/ ]{6,20}$");
    Pattern userNamePattern =  Pattern.compile("^[A-z]{5,10}$");
    Pattern passwordPattern =  Pattern.compile("^[A-z|0-9|\\W][\\S]{7,12}$");
    public void initialize(){
        cmbRole.getItems().addAll("Librarian","User");
        btnCreateAccount.setDisable(true);
        validateInit();
        changeVisibility();

    }
    void changeVisibility(){
        chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(chkBox.isSelected()){
                txtPasswordView.setText(txtPassword.getText());
                txtPasswordView.setVisible(true);
                txtPassword.setVisible(false);
                return;
            }
            txtPassword.setText(txtPasswordView.getText());
            txtPassword.setVisible(true);
            txtPasswordView.setVisible(false);
        });
    }

    private void validateInit() {
        map.put(txtName,namePattern);
        map.put(txtMobile,mobilePattern);
        map.put(txtAddress,addressPattern);
        map.put(txtUserName,userNamePattern);
        map.put(txtPassword,passwordPattern);

    }


    public void backToOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) signUpContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
    }

    public void validateFieldOnPress(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnCreateAccount);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }
        }
    }

    public String getUserId() throws SQLException, ClassNotFoundException {
       ResultSet rst= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM Users ORDER BY userId DESC LIMIT 1"

        ).executeQuery();
       if (rst.next()) {
           int tempId = Integer.parseInt(rst.getString(1).split("-")[1]);
           tempId = tempId+1;
           if(tempId<=9){
               return "U-00"+tempId;
           }else if(tempId<=99){
               return "U-0"+tempId;
           }else {
               return "U-"+tempId;
           }
       }else{
           return "U-001";
       }
    }

    public void accountCreateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("INSERT INTO Users VALUE (?,?,?,?,?,?,?)");
        pstm.setObject(1,getUserId());
        pstm.setObject(2,txtName.getText());
        pstm.setObject(3,txtUserName.getText());
        pstm.setObject(4,txtPassword.getText());
        pstm.setObject(5,txtMobile.getText());
        pstm.setObject(6,txtAddress.getText());
        pstm.setObject(7,cmbRole.getValue());

        int save = pstm.executeUpdate();
        if(save == 1){
            new Alert(Alert.AlertType.CONFIRMATION,"Successfully entered data").show();
            txtName.clear();
            txtPassword.clear();
            txtAddress.clear();
            txtMobile.clear();
            txtUserName.clear();
            txtPassword.clear();
            txtPasswordView.clear();
        }else {
            new Alert(Alert.AlertType.WARNING,"Something went Wrong").show();
        }

    }
}
