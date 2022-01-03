package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.Login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    public AnchorPane mainLoginContext;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public Label lblLog;
    public Label lblErrorMsg;
    public CheckBox chkBox;
    public JFXTextField passwordView;
    public JFXButton btnLogin;
    public AnchorPane secondContext;

    public void initialize(){
        changeVisibility();

    }


    void changeVisibility(){
        chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(chkBox.isSelected()){
                passwordView.setText(txtPassword.getText());
                passwordView.setVisible(true);
                txtPassword.setVisible(false);

            }else {
                txtPassword.setText(passwordView.getText());
                txtPassword.setVisible(true);
                passwordView.setVisible(false);
            }
        });
    }


    public void adminLoginFormOnAction(ActionEvent actionEvent) {
        lblLog.setText("Librarian");
    }

    public void userLoginFormOnAction(ActionEvent actionEvent) {
        lblLog.setText("User");
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {

        if(loginValidate(txtUserName.getText(), txtPassword.getText(), lblLog.getText())) {
            if (lblLog.getText().equals("User")) {
                Login.login = "User";
                Login.userName =txtUserName.getText();
                Login.password = txtPassword.getText();
                Stage window = (Stage) mainLoginContext.getScene().getWindow();
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashBoardForm.fxml"))));
            }else{
                Login.login ="Admin";
                Login.userName =txtUserName.getText();
                Login.password = txtPassword.getText();
                Stage window = (Stage) mainLoginContext.getScene().getWindow();
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashBoardForm.fxml"))));
            }
        }else{
            lblErrorMsg.setText("Please enter the correct data ");
        }
    }

    private boolean loginValidate(String userName, String password, String role) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM users WHERE userName=? AND userPassword =? AND role=?");
        pstm.setObject(1,userName);
        pstm.setObject(2,password);
        pstm.setObject(3,role);

        ResultSet rst = pstm.executeQuery();
        if(rst.next()) {
            return true;
        }else {
            return false;
        }
    }

    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) mainLoginContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/CreateAccountForm.fxml"))));
    }

    public void focusToPassword(KeyEvent keyEvent) {
        if(keyEvent.getCode()== KeyCode.ENTER){
            txtPassword.requestFocus();
        }
    }
}
