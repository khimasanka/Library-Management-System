package model;

public class Books {
    private String id;
    private String bookName;
    private String bookType;
    private String language;
    private String authorName;
    private int count;

    public Books() {
    }

    public Books(String id, String bookName, String bookType, String language, String authorName, int count) {
        this.setId(id);
        this.setBookName(bookName);
        this.setBookType(bookType);
        this.setLanguage(language);
        this.setAuthorName(authorName);
        this.setCount(count);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id='" + id + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookType='" + bookType + '\'' +
                ", language='" + language + '\'' +
                ", authorName='" + authorName + '\'' +
                ", count=" + count +
                '}';
    }
}
