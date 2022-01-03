package model;

public class DonateDetails {
    private String donateId;
    private String name;
    private String bookName;
    private String date;
    private int qty ;

    public DonateDetails() {
    }

    public DonateDetails(String donateId, String name, String bookName, int qty) {
        this.donateId = donateId;
        this.name = name;
        this.bookName = bookName;
        this.qty = qty;
    }

    public DonateDetails(String donateId, String name, String bookName, String date, int qty) {
        this.donateId = donateId;
        this.name = name;
        this.bookName = bookName;
        this.date = date;
        this.qty = qty;
    }

    public String getDonateId() {
        return donateId;
    }

    public void setDonateId(String donateId) {
        this.donateId = donateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "DonateDetails{" +
                "donateId='" + donateId + '\'' +
                ", name='" + name + '\'' +
                ", bookName='" + bookName + '\'' +
                ", date='" + date + '\'' +
                ", qty=" + qty +
                '}';
    }
}
