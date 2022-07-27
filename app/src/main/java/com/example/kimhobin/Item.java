package com.example.kimhobin;

public class Item {
    String title, imgName, people,url;
    double rating;
    int genre;

    public Item(String title,  String imgName, String people, int genre, String url, double rating) {
        this.title = title;
        this.imgName = imgName;
        this.people = people;
        this.genre = genre;
        this.url = url;
        this.rating =rating;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }
}
