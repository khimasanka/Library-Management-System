package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Users;
import util.Login;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SettingsFormController {
    public Label lblName;
    public TextField txtFullName;
    public TextField txtAddress;
    public TextField txtMobile;
    public Label lblRole;
    public Label lblUserId;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public TextField txtPasswordShow;
    public PasswordField txtNewPassword;
    public CheckBox chkBox;
    public AnchorPane settingsContext;
    public JFXButton btnSaveChanges;


    LinkedHashMap<TextField,Pattern> map = new LinkedHashMap<>();

    public void initialize(){
       setUserDetails();
       changeVisibility();
       storeValidations();
       btnSaveChanges.setDisable(true);
    }

    private void storeValidations() {
        map.put(txtFullName,Pattern.compile("^[A-z ]{3,25}$"));
        map.put(txtAddress,Pattern.compile("^[A-z0-9 /, ]{6,50}$"));
        map.put(txtMobile,Pattern.compile("^0?(7)[0|1|2|4|5|6|7|8]-?[0-9]{7}$"));
        map.put(txtUserName,Pattern.compile("^[A-z]{5,10}$"));
        map.put(txtPassword,Pattern.compile("^[A-z|0-9|\\W][\\S]{7,12}$"));
        map.put(txtNewPassword,Pattern.compile("^[A-z|0-9|\\W][\\S]{7,12}$"));
    }

    public void userAccountsFieldsFocus(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnSaveChanges);
        if (keyEvent.getCode()==KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){

            }
        }
    }

    public void setUserDetails(){
        Users users = null;
        try {
            users = SettingsController.getUser(Login.userName);

           lblName.setText(users.getUserName());
           lblRole.setText(users.getRole());
           lblUserId.setText(users.getUserid());
           txtFullName.setText(users.getUserFullName());
           txtAddress.setText(users.getUserAddress());
           txtMobile.setText(String.valueOf(users.getMobile()));
           txtUserName.setText(users.getUserName());

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    void changeVisibility(){
        chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(chkBox.isSelected()){
                txtPasswordShow.setText(txtNewPassword.getText());
                txtPasswordShow.setVisible(true);
                txtNewPassword.setVisible(false);

            }else {
                txtNewPassword.setText(txtPasswordShow.getText());
                txtNewPassword.setVisible(true);
                txtPasswordShow.setVisible(false);
            }
        });
    }

    public void saveChangesOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(txtPassword.getText().equals(Login.password)){
            Users users = new Users(
                    lblUserId.getText(),
                    txtFullName.getText(),
                    txtUserName.getText(),
                    txtNewPassword.getText(),
                    Integer.parseInt(txtMobile.getText()),
                    txtAddress.getText(),
                    lblRole.getText()
            );

            if( SettingsController.changeUserDetails(users)){
                new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
                Login.password = txtNewPassword.getText();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again..").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Password is a mismatch").show();
        }
    }

    public void removeAccount(ActionEvent actionEvent) {
        try {
            if(SettingsController.deleteUserAccount(lblUserId.getText())){
                Stage window = (Stage) settingsContext.getScene().getWindow();
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
            }
        } catch (SQLException | ClassNotFoundException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

}
