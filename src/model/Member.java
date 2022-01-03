package model;

public class Member {
    private String id;
    private String name;
    private String Address;
    private String nic;
    private int mobile;

    public Member() {
    }

    public Member(String id, String name, String address, String nic, int mobile) {
        this.setId(id);
        this.setName(name);
        setAddress(address);
        this.setNic(nic);
        this.setMobile(mobile);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", Address='" + getAddress() + '\'' +
                ", nic='" + getNic() + '\'' +
                ", mobile=" + getMobile() +
                '}';
    }
}
