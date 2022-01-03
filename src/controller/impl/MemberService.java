package controller.impl;

import model.Member;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MemberService {
    public boolean saveMember(Member m) throws SQLException, ClassNotFoundException;
    public boolean updateMember(Member m) throws SQLException, ClassNotFoundException;
    public boolean deleteMember(String id) throws SQLException, ClassNotFoundException, IOException;
    public Member searchMember(String id) throws SQLException, ClassNotFoundException;
    public ArrayList<Member> getAllMembers( ) throws SQLException, ClassNotFoundException;
}
