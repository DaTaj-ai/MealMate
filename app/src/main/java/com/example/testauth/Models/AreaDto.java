package com.example.testauth.Models;

import com.google.gson.annotations.SerializedName;

public class AreaDto {

    @SerializedName("strArea")
    String strArea ;

    public AreaDto(String strArea) {
        this.strArea = strArea;
    }

    public AreaDto() {
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }
}
