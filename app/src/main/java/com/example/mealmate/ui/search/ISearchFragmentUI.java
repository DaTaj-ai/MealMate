package com.example.testauth.ui.search;

import com.example.testauth.Models.MealDto;

import java.util.List;

public interface ISearchFragmentUI {

    void showMeals(List<MealDto> meals);
    void showError(String message);

}
