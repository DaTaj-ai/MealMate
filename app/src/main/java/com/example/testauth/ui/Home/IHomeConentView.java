package com.example.testauth.ui.Home;

import com.example.testauth.Models.CategoryDto;
import com.example.testauth.Models.MealDto;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public interface IHomeConentView {
    void showInsperationMeal(List<MealDto> meals);
    void showCategory(List<String> items);
    void showIngrdients(List<String> items);
    void showArea(List<String> items);

    void showCategoryMeals(List<MealDto> meals);
    void showInspricarionMeal(MealDto mealDto);

}
