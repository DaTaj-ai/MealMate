package com.example.testauth.Models;

public class IngredientDtoView {

    public IngredientDtoView(String measure, String ingredient) {
        this.measure = measure;
        this.ingredient = ingredient;

    }

    private String ingredient;
    private String measure;

    private String strIngredientThumb;

    public String getStrIngredientThumb() {
        return strIngredientThumb;
    }

    public void setStrIngredientThumb(String strIngredientThumb) {
        this.strIngredientThumb = strIngredientThumb;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
