package view.tm;

public class MemberTM {
    private String id;
    private String name;
    private String address;
    private String nic;
    private int mobile;

    public MemberTM() {
    }

    public MemberTM(String id, String name, String address, String nic, int mobile) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
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
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return "MemberTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", nic='" + nic + '\'' +
                ", mobile=" + mobile +
                '}';
    }
}
