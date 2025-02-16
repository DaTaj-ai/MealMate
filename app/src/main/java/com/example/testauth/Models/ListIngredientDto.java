package com.example.testauth.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListIngredientDto {

    @SerializedName("meals")
    List<IngredientDto> ingredientDtoList ;

    public ListIngredientDto(List<IngredientDto> ingredientDtoList) {
        this.ingredientDtoList = ingredientDtoList;
    }

    public List<IngredientDto> getIngredientDtoList() {
        return ingredientDtoList;
    }

    public void setIngredientDtoList(List<IngredientDto> ingredientDtoList) {
        this.ingredientDtoList = ingredientDtoList;
    }

    public List<String> toList(){
        List<String> locallist = new ArrayList<>() ;
        for(IngredientDto ing : ingredientDtoList ){
            locallist.add(ing.getStrIngredient());
        }
        return locallist;
    }
}
