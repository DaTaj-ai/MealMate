package com.example.testauth.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryDto {

    @SerializedName("meals")
    List<CategoryDto> categoryDtoList ;

    public ListCategoryDto(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

    public List<CategoryDto> getCategoryDtoList() {
        return categoryDtoList;
    }

    public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

    public List<String> toList(){
        List<String> locallist = new ArrayList<>() ;
        for(CategoryDto cat : categoryDtoList){
           locallist.add(cat.getStrCategory());
        }
        return locallist;
    }
}
