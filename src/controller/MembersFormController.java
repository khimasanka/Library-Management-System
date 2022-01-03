package controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Member;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.ValidationUtil;
import view.tm.MemberTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class MembersFormController  {
    public TextField txtNic;
    public TextField txtAddress;
    public JFXButton btnAddMember;
    public TextField txtMemberName;
    public Text lblMemberId;
    public TextField txtMobile;
    public TextField txtMemberSearch;
    public TableView<MemberTM> tblMember;
    public TableColumn colMemberId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colNic;
    public TableColumn colMobile;
    public AnchorPane memberContext;
    public JFXButton btnClearField;
    public JFXButton btnUpdateMember;
    public JFXButton btnPrintACard;

    ObservableList <MemberTM> obList;
    LinkedHashMap<TextField , Pattern> map = new LinkedHashMap<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        tblMember.getItems().setAll(loadAllMembers());
        setMemberId();
        setTableData();

        memberValidation();
        btnAddMember.setDisable(true);
        btnUpdateMember.setDisable(true);

        txtMemberSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                memberSearchDataToTable(newValue);
            }
        });

    }

    public void removeMemberOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(new MemberController().deleteMember(lblMemberId.getText())){
            tblMember.getItems().setAll(loadAllMembers());
            cleanFields();
            setMemberId();
        }else {
        }
    }

    public void addMemberOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Member m1 = new Member(
                lblMemberId.getText(),
                txtMemberName.getText(),
                txtAddress.getText(),
                txtNic.getText(),
                Integer.parseInt(txtMobile.getText())

        );
        if (new MemberController().saveMember(m1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Successful").show();
            setMemberId();
            tblMember.getItems().setAll(loadAllMembers());
            cleanFields();
            btnAddMember.setDisable(true);
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private void cleanFields(){
        txtMemberName.clear();
        txtNic.clear();
        txtMobile.clear();
        txtAddress.clear();
        txtMemberSearch.clear();
    }

    private void setMemberId(){
        try {
            lblMemberId.setText(MemberController.getMemberId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void editMemberDetailOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Member m1 = new Member(
                lblMemberId.getText(),
                txtMemberName.getText(),
                txtAddress.getText(),
                txtNic.getText(),
                Integer.parseInt(txtMobile.getText())
        );

        if(new MemberController().updateMember(m1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
            tblMember.getItems().setAll(loadAllMembers());
            loadAllMembers();
            btnUpdateMember.setDisable(true);
            cleanFields();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void memberSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
      memberSearchDataToTable(txtMemberSearch.getText());
    }

    public ObservableList<MemberTM> loadAllMembers() throws SQLException, ClassNotFoundException {
        List<Member> allMembers = new MemberController().getAllMembers();

        obList = FXCollections.observableArrayList();
        for (Member member: allMembers) {
            obList.add(new MemberTM(
                    member.getId(),
                    member.getName(),
                    member.getAddress(),
                    member.getNic(),
                    member.getMobile()
            ));
        }
        return obList;
    }

    void setTableData(){
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));

    }

    public void memberFieldClearOnAction(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        String id = tblMember.getSelectionModel().getSelectedItem().getId();
        boolean isDeleted = new MemberController().deleteMember(id);
        if(isDeleted) {
            tblMember.getItems().setAll(loadAllMembers());
            cleanFields();
            setMemberId();
        }

    }

    private void memberSearchDataToTable(String value){
        try {
            List<Member> members = MemberController.searchMemberToTable(value);
            ObservableList<MemberTM> tableData = FXCollections.observableArrayList();
                for (Member member : members) {
                    tableData.add(new MemberTM(
                            member.getId(),
                            member.getName(),
                            member.getAddress(),
                            member.getNic(),
                            member.getMobile()
                    ));
                    lblMemberId.setText(member.getId());
                    txtMemberName.setText(member.getName());
                    txtAddress.setText(member.getAddress());
                    txtNic.setText(member.getNic());
                    txtMobile.setText(String.valueOf(member.getMobile()));
                    btnUpdateMember.setDisable(false);
                }

                tblMember.getItems().setAll(tableData);

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void memberValidation(){
        map.put(txtMemberName,Pattern.compile("^[A-z][A-z.]+([\\ A-z][A-z.]+)*$"));
        map.put(txtAddress,Pattern.compile("^[A-z][A-z,.]+([\\ A-z/][A-z./,\\d]+)*$"));
        map.put(txtNic,Pattern.compile("^([3-9][0-9]{8}(v|V))|([1-2][0-9][0-9]{10})$"));
        map.put(txtMobile,Pattern.compile("^0?(7)[0|1|2|4|5|6|7|8]-?[0-9]{7}$"));
    }

    public void memberFieldsFocus(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(map,btnAddMember);
        if (keyEvent.getCode()== KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){

            }
        }
    }

    public void printACardOnAction(MouseEvent mouseEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/PrintACard.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            String memberId = lblMemberId.getText();
            String memberName = txtMemberName.getText();
            String localDate = LocalDate.now().toString();

            HashMap map = new HashMap();
            map.put("id",memberId);
            map.put("name",memberName);
            map.put("date",localDate);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,map,new JREmptyDataSource());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
