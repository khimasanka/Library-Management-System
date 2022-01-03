package model;

public class BookCase {
    private String caseId;
    private String bookType;
    private String bookLanguage;

    public BookCase() {
    }

    public BookCase(String caseId, String bookType, String bookLanguage) {
        this.caseId = caseId;
        this.bookType = bookType;
        this.bookLanguage = bookLanguage;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    @Override
    public String toString() {
        return "BookCase{" +
                "caseId='" + caseId + '\'' +
                ", bookType='" + bookType + '\'' +
                ", bookLanguage='" + bookLanguage + '\'' +
                '}';
    }
}
