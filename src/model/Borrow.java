package model;

import java.util.ArrayList;

public class Borrow {
    private String borrowId;
    private String memberId;
    private String memName ;
    private String borrowDate;
    private String borrowTime ;
    private String reserveBook;
    private ArrayList<BorrowBooksDetail> books;

    public Borrow() {
    }

    public Borrow(String borrowId, String memberId, String memName, String borrowDate, String borrowTime, String reserveBook, ArrayList<BorrowBooksDetail> books) {
        this.borrowId = borrowId;
        this.memberId = memberId;
        this.memName = memName;
        this.borrowDate = borrowDate;
        this.borrowTime = borrowTime;
        this.setReserveBook(reserveBook);
        this.books = books;
    }

    public Borrow(String borrowId, String memberId, String memName, String borrowDate, String borrowTime, ArrayList<BorrowBooksDetail> books) {
        this.borrowId = borrowId;
        this.memberId = memberId;
        this.memName = memName;
        this.borrowDate = borrowDate;
        this.borrowTime = borrowTime;
        this.books = books;
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

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }


    public ArrayList<BorrowBooksDetail> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<BorrowBooksDetail> books) {
        this.books = books;
    }

    public String getReserveBook() {
        return reserveBook;
    }

    public void setReserveBook(String reserveBook) {
        this.reserveBook = reserveBook;
    }
}
