package controller;

import controller.impl.MemberService;
import db.DbConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberController implements MemberService {
    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    @Override
    public boolean saveMember(Member m) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO member VALUES (?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,m.getId());
        stm.setObject(2, m.getName());
        stm.setObject(3, m.getAddress());
        stm.setObject(4, m.getNic());
        stm.setObject(5, m.getMobile());

        return stm.executeUpdate()>0;

    }

    @Override
    public boolean updateMember(Member m) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "UPDATE member SET memName=? ,NIC=? ,address=? , mobile=? WHERE memberId=?");
        stm.setObject(1,m.getName());
        stm.setObject(2,m.getNic());
        stm.setObject(3,m.getAddress());
        stm.setObject(4,m.getMobile());
        stm.setObject(5,m.getId());
        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteMember(String id) throws SQLException, ClassNotFoundException {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Delete.?",yes,no);
        alert.setTitle("Delete Member");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes) {
            if (DbConnection.getInstance().getConnection().prepareStatement(
                    "DELETE FROM member WHERE memberId='" + id + "'"
            ).executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        }else {

        }return false;
    }

    @Override
    public Member searchMember(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement stm= DbConnection.getInstance().getConnection().
                prepareStatement("SELECT * FROM member WHERE memberID=?");
        stm.setObject(1,id);
        ResultSet rst = stm.executeQuery();

        if (rst.next()) {
            return new Member(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5)
            );

        }else{
            return null;
        }
    }

    @Override
    public ArrayList<Member> getAllMembers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM member");
        ResultSet rst = stm.executeQuery();
        ArrayList<Member>members =new ArrayList<>();
        while (rst.next()){
            members.add(new Member(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getInt(5)
            ));
        }
        return members;
    }

    public static List<Member> searchMemberToTable(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM member WHERE member.memberId LIKE '" + value + "%' OR member.memName LIKE '" + value + "%'");
        ResultSet rs = pstm.executeQuery();

        List<Member> members = new ArrayList<>();

        while (rs.next()) {
            members.add(new Member(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getInt(5)
            ));
        }

        return members;
    }

    public static String getMemberId() throws SQLException, ClassNotFoundException {
        ResultSet rs= DbConnection.getInstance().getConnection().prepareStatement(
                "SELECT * FROM member ORDER BY memberId DESC LIMIT 1"

        ).executeQuery();
        if (rs.next()) {
            int tempId = Integer.parseInt(rs.getString(1).split("-")[1]);
            tempId = tempId+1;
            if(tempId<=9){
                return "M-0000"+tempId;
            }else if(tempId<=99){
                return "M-000"+tempId;
            }else if(tempId<=999){
                return "M-00"+tempId;
            }else if(tempId<=9999){
                return "M-0"+tempId;
            }else{
                return "M-"+tempId;
            }
        }else{
            return "M-00001";
        }
    }
}
