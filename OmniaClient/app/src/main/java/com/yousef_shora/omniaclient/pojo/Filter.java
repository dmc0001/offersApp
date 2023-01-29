package com.yousef_shora.omniaclient.pojo;

public class Filter {
    int image;
    String name;
    boolean isSelected;

    public Filter(int image, String name) {
        this.image = image;
        this.name = name;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

}
