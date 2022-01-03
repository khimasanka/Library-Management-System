package view.tm;

public class BookCaseTM {
    private String caseId;
    private String bookType;
    private String language;

    public BookCaseTM() {
    }

    public BookCaseTM(String caseId, String bookType, String language) {
        this.caseId = caseId;
        this.bookType = bookType;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
