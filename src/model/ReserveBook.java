package model;

public class ReserveBook {
    private String borrowId;
    private String memberId;
    private String memberName;
    private String bookId;
    private String borrowDate;
    private String bookName;
    private String reserveDate;

    public ReserveBook() {
    }

    public ReserveBook(String borrowId, String reserveDate) {
        this.borrowId = borrowId;
        this.reserveDate = reserveDate;
    }

    public ReserveBook(String borrowId, String memberId, String memberName, String bookId, String bookName) {
        this.borrowId = borrowId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public ReserveBook(String borrowId, String memberId, String memberName, String bookId, String borrowDate, String bookName, String reserveDate) {
        this.borrowId = borrowId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.bookName = bookName;
        this.reserveDate = reserveDate;
    }


    public ReserveBook(String borrowId, String memberId, String memberName, String bookId, String borrowDate, String reserveDate) {
        this.borrowId = borrowId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.reserveDate = reserveDate;
    }




    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
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

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }
}
