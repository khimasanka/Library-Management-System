package model;

import java.util.ArrayList;

public class Donate {
    private String donateId;
    private String name;
    private String date;
    private ArrayList<DonateBooks> books;

    public Donate() {
    }

    public Donate(String donateId, String name, String date, ArrayList<DonateBooks> books) {
        this.donateId = donateId;
        this.name = name;
        this.date = date;
        this.books = books;
    }

    public String getDonateId() {
        return donateId;
    }

    public void setDonateId(String donateId) {
        this.donateId = donateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<DonateBooks> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<DonateBooks> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Donate{" +
                "donateId='" + donateId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", books=" + books +
                '}';
    }
}
