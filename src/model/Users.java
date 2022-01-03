package model;

public class Users {
    private String userid;
    private String userFullName;
    private String userName;
    private String password;
    private int mobile;
    private String userAddress;
    private String role;

    public Users() {
    }

    public Users(String userid, String userFullName, String userName, String password, int mobile, String userAddress, String role) {
        this.userid = userid;
        this.userFullName = userFullName;
        this.userName = userName;
        this.password = password;
        this.mobile = mobile;
        this.userAddress = userAddress;
        this.role = role;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userid='" + userid + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", mobile=" + mobile +
                ", userAddress='" + userAddress + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
