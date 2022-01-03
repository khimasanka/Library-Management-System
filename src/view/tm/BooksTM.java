package view.tm;

public class BooksTM {
    private String id;
    private String name;
    private String category;
    private String language;
    private String author;
    private int qty;

    public BooksTM() {
    }

    public BooksTM(String id, String name, String category, String language, String author, int qty) {
        this.setId(id);
        this.setName(name);
        this.setCategory(category);
        this.setLanguage(language);
        this.setAuthor(author);
        this.setQty(qty);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "BooksTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", language='" + language + '\'' +
                ", author='" + author + '\'' +
                ", qty=" + qty +
                '}';
    }
}
