package com.example.testauth.Network;


import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiMealOperations {


    @GET("search.php")
    Observable<ListMealDto> getMeals(@Query("s") String search);


    @GET("random.php")
    Observable<ListMealDto> getRandomMeals();

//    //Categories,
    @GET("list.php")
    Observable<ListCategoryDto> getAllCategories(@Query("c") String categoryType);

   // Area,
    @GET("list.php")
    Observable<ListAreaDto> getAllAreas(@Query("a")  String areaType);

    // Ingredients
    @GET("list.php")
    Observable<ListIngredientDto> getAllIngredients(@Query("i") String ingredientType);

    @GET("filter.php")
    Observable<ListMealDto> filterByArea(@Query("a")  String areaType);

    @GET("filter.php")
    Observable<ListMealDto> filterByCategory(@Query("c")  String areaType);

    @GET("filter.php")
    Observable<ListMealDto> filterByIngredient(@Query("i")  String areaType);


}

