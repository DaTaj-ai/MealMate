package com.example.testauth.Repository.datasources;

import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface MealRemoteDataSource {
    /*
 -----------------------------------------------------------------------
  $$$$$$$$$$$$$$           Meal Api functions           $$$$$$$$$$$$$$$$$
 -----------------------------------------------------------------------

 */
    public Observable<ListMealDto> getMeals(String query);

    public Observable<ListMealDto> getRandomMeal();
    public Observable<ListMealDto> getMealById(String query);

    /*
 -----------------------------------------------------------------------
  $$$$$$$$$$$$$$           Lists  functions           $$$$$$$$$$$$$$$$$
 -----------------------------------------------------------------------

 */

    Observable<ListCategoryDto> getAllCategories();

    Observable<ListAreaDto> getAllAreas();

    Observable<ListIngredientDto> getAllIngredients();


    /*
 -----------------------------------------------------------------------
  $$$$$$$$$$$$$$           FireBase functions           $$$$$$$$$$$$$$$$$
 -----------------------------------------------------------------------

 */
    public DatabaseReference getUserCalenderFireBase();

    public DatabaseReference getMealsFromFireBase();

    public void insertMealToFireBase(MealDto mealDto);

    void insertCalenderFireBase(MealsCalenderDto mealsCalenderDto);


    /*
     -----------------------------------------------------------------------
      $$$$$$$$$$$$$$           Filter functions           $$$$$$$$$$$$$$$$$
     -----------------------------------------------------------------------

     */
    public Observable<ListMealDto> filterByCategory(String categoryType);

    public Observable<ListMealDto> filterByIngredients(String ingredientType);

    public Observable<ListMealDto> filterByArea(String areaType);


}
