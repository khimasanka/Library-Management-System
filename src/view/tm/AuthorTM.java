package view.tm;

public class AuthorTM {
    private String authorId;
    private String name;

    public AuthorTM() {
    }

    public AuthorTM(String authorId, String name) {
        this.setAuthorId(authorId);
        this.setName(name);
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorTM{" +
                "authorId='" + authorId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
