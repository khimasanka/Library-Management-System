package model;

public class DonateBooks {
    private String donateId;
    private String bookName;
    private int qty;

    public DonateBooks() {
    }

    public DonateBooks(String donateId, String bookName, int qty) {
        this.donateId = donateId;
        this.bookName = bookName;
        this.qty = qty;
    }

    public String getDonateId() {
        return donateId;
    }

    public void setDonateId(String donateId) {
        this.donateId = donateId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "DonateBooks{" +
                "donateId='" + donateId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", qty=" + qty +
                '}';
    }
}
