package com.example.testauth.Repository;

import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.Repository.datasources.MealLocalDataSource;
import com.example.testauth.Repository.datasources.MealRemoteDataSource;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class RepositoryImpl implements Repository {

    private MealRemoteDataSource remoteDataSourceMeal;
    private MealLocalDataSource localDataSourceMeal;
    private static RepositoryImpl instance;

    /* ----------------------------------
                    Handel Caching
       ----------------------------------
     */
    private Observable<ListMealDto> cachedGetRemoteMeals;
    private Observable<ListMealDto> cachedGetRandomMeal;


    public static RepositoryImpl getInstance(MealRemoteDataSource RemoteDataSourceMeal, MealLocalDataSource LocalDataSourceMeal) {
        if (instance == null) {
            instance = new RepositoryImpl(RemoteDataSourceMeal, LocalDataSourceMeal);
        }
        return instance;
    }

    public RepositoryImpl(MealRemoteDataSource RemoteDataSourceMeal, MealLocalDataSource LocalDataSourceMeal) {
        this.remoteDataSourceMeal = RemoteDataSourceMeal;
        this.localDataSourceMeal = LocalDataSourceMeal;
    }

    /*
 -----------------------------------------------------------------------
  $$$$$$$$$$$$$$           Local functions           $$$$$$$$$$$$$$$$$
 -----------------------------------------------------------------------
                                                                    */

    public Completable insertLocal(MealDto mealDto) {
        return localDataSourceMeal.insert(mealDto);
    }

    public Completable deleteLocal(MealDto mealDto) {
        return  localDataSourceMeal.delete(mealDto);
    }

    public Flowable<List<MealDto>> getAllLocal() {
        return localDataSourceMeal.getAllLocalMeals();
    }


    @Override
    public Completable insertToCalender(MealsCalenderDto mealCalenderDto) {
        return localDataSourceMeal.insertToCalender(mealCalenderDto);
    }

    @Override
    public Completable DeleteFromCalender(MealsCalenderDto mealCalenderDto) {
        return localDataSourceMeal.DeleteFromCalender(mealCalenderDto);
    }

    public Flowable<List<MealDto>> getAllIsFavorites() {
        return localDataSourceMeal.getAllIsFavorites();
    }

    @Override
    public Flowable<List<String>> getMealIdsForDate(String date) {
        return localDataSourceMeal.getMealIdsForDate(date);
    }

    @Override
    public Flowable<List<MealDto>> getMealsByIds(List<String> mealIds) {
        return localDataSourceMeal.getMealsByIds(mealIds);
    }



    /*
-----------------------------------------------------------------------
  $$$$$$$$$$$$$$           Remote functions           $$$$$$$$$$$$$$$$$
-----------------------------------------------------------------------
                                                                    */
public Observable<ListMealDto> getRemoteMeals(String query) {
    return remoteDataSourceMeal.getMeals(query);
}

public Observable<ListMealDto> getMealByIdRemote(String query) {
    return remoteDataSourceMeal.getMealById(query);
}

public Observable<ListMealDto> getRandomMeal() {
        if (cachedGetRandomMeal == null) {
            cachedGetRandomMeal = remoteDataSourceMeal.getRandomMeal();
        }
        return cachedGetRandomMeal;
    }





    /*
  -----------------------------------------------------------------------
  $$$$$$$$$$$$$$           List Functions           $$$$$$$$$$$$$$$$$
 -----------------------------------------------------------------------


     */

    public Observable<ListCategoryDto> getAllCategories() {
        return remoteDataSourceMeal.getAllCategories();
    }

    public Observable<ListAreaDto> getAllAreas() {
        return remoteDataSourceMeal.getAllAreas();
    }

    public Observable<ListIngredientDto> getAllIngredients() {
        return remoteDataSourceMeal.getAllIngredients();
    }

    /*
 -----------------------------------------------------------------------
  $$$$$$$$$$$$$$           Filters Functions           $$$$$$$$$$$$$$$$$
 -----------------------------------------------------------------------
                                                                    */

    public Observable<ListMealDto> filterByArea(String areaType) {
        return remoteDataSourceMeal.filterByArea(areaType);
    }
    public Observable<ListMealDto> filterByIngredients(String ingredientType) {
        return remoteDataSourceMeal.filterByIngredients(ingredientType);
    }

    public Observable<ListMealDto> filterByCategory(String categoryType) {
        return remoteDataSourceMeal.filterByCategory(categoryType);
    }






    /*
    ----------------------------------------------------------------------
    $$$$$$$$$$$$$$           FireBase functions           $$$$$$$$$$$$$$$$
    ----------------------------------------------------------------------
                                                                      */
    public void insertMealToFireBase(MealDto mealDto) {
        remoteDataSourceMeal.insertMealToFireBase(mealDto);
    }

    public void insertCalenderFireBase(MealsCalenderDto mealsCalenderDto) {
        remoteDataSourceMeal.insertCalenderFireBase(mealsCalenderDto);
    }

    public void getUserMealsFromFireBase() {
        remoteDataSourceMeal.getMealsFromFireBase();
    }

    @Override
    public DatabaseReference getCalenderFromFireBase() {
        return remoteDataSourceMeal.getUserCalenderFireBase();

    }

    @Override
    public DatabaseReference getMealsFromFireBase() {
        return remoteDataSourceMeal.getMealsFromFireBase();
    }

}
