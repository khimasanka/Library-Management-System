package model;

public class BorrowBooksDetail {
    private String bookId ;
    private String bookName ;
    private String borrowDate ;

    public BorrowBooksDetail() {
    }

    public BorrowBooksDetail(String bookId, String bookName, String borrowDate) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.borrowDate = borrowDate;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    @Override
    public String toString() {
        return "BorrowBooksDetail{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", borrowDate='" + borrowDate + '\'' +
                '}';
    }
}
