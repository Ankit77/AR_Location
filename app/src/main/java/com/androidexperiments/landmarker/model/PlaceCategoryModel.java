package com.androidexperiments.landmarker.model;

/**
 * Created by ANKIT on 3/1/2017.
 */

public class PlaceCategoryModel {
    private String placeCategory;
    private String placeUrl;
    private boolean isSelect;

    public PlaceCategoryModel() {
    }

    public PlaceCategoryModel(String placeCategory, String placeUrl) {
        this.placeCategory = placeCategory;
        this.placeUrl = placeUrl;
        this.isSelect = false;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(String placeCategory) {
        this.placeCategory = placeCategory;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }
}
