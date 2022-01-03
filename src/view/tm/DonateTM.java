package view.tm;

public class DonateTM {
    private String donateID;
    private String personName;
    private String bookName;
    private String date;
    private int qty;

    public DonateTM() {
    }

    public DonateTM(String donateID, String personName, String bookName, String date, int qty) {
        this.setDonateID(donateID);
        this.setPersonName(personName);
        this.setBookName(bookName);
        this.setDate(date);
        this.setQty(qty);
    }



    public String getDonateID() {
        return donateID;
    }

    public void setDonateID(String donateID) {
        this.donateID = donateID;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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
        return "DonateTM{" +
                "donateID='" + getDonateID() + '\'' +
                ", personName='" + getPersonName() + '\'' +
                ", bookName='" + getBookName() + '\'' +
                ", date='" + getDate() + '\'' +
                ", qty=" + getQty() +
                '}';
    }
}
