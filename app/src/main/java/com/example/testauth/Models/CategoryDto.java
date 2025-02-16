package com.example.testauth.Models;

import com.google.gson.annotations.SerializedName;

public class CategoryDto {

    @SerializedName("strCategory")
    String strCategory ;

    public CategoryDto(String strCategory) {
        this.strCategory = strCategory;
    }

    public CategoryDto() {
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }
}
