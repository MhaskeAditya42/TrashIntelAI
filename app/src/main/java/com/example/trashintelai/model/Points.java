package com.example.trashintelai.model;

public class Points {
    private int image ;
    private String name, points;

    public Points(int image, String name, String points) {
        this.image = image;
        this.name = name;
        this.points = points;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
