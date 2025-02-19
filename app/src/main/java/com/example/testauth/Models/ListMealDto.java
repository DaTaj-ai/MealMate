package com.example.testauth.Models;

import java.util.List;

public class ListMealDto {

    List<MealDto> meals;

    public ListMealDto(List<MealDto> meals) {
        this.meals = meals;
    }
    public ListMealDto() {
    }

    public List<MealDto> getMeals() {
        return meals;
    }

    public void setMeals(List<MealDto> meals) {
    }
}
