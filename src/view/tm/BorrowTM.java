package view.tm;

public class BorrowTM {
    private String bookId;
    private String bookName;
    private String memberId;
    private String memberName;
    private String borrowDate;

    public BorrowTM() {
    }

    public BorrowTM(String bookId, String bookName, String memberId, String memberName, String borrowDate) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.memberId = memberId;
        this.memberName = memberName;
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

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    @Override
    public String toString() {
        return "BorrowTM{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", borrowDate='" + borrowDate + '\'' +
                '}';
    }
}
