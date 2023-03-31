package com.example.lightbrains.part_first_mental.flashanzan;

public class RecyclerViewItem {
    private int imageResource;
    private String titleName;
    private int colorResource;

    public int getImageResource() {
        return imageResource;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getColorResource() {
        return colorResource;
    }

    public void setColorResource(int colorResource) {
        this.colorResource = colorResource;
    }

    public RecyclerViewItem(int imageResource, String titleName, int colorResource) {
        this.imageResource = imageResource;
        this.titleName = titleName;
        this.colorResource = colorResource;
    }
}
