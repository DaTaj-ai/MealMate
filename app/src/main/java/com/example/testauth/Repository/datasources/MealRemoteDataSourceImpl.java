package com.example.testauth.Repository.datasources;

import android.util.Log;

import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.Network.ApiMealOperations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MealRemoteDataSourceImpl implements MealRemoteDataSource {


    // Meal API
    public static final String URL = "https://www.themealdb.com/api/json/v1/1/";
    public static MealRemoteDataSourceImpl instance;
    List<MealDto> MealDtos = new ArrayList<>(Arrays.asList(new MealDto()));
    ApiMealOperations apiMealOperations;

    // Fire Base
    FirebaseDatabase fireBasedatabase;
    DatabaseReference fireBasereference;

    private static final String TAG = "MealRemoteDataSourceImp";

    public static MealRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new MealRemoteDataSourceImpl();
        }
        return instance;
    }


    public MealRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(URL).
                addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
        apiMealOperations = retrofit.create(ApiMealOperations.class);
    }


    public Observable<ListMealDto> getMeals(String query) {
        Observable<ListMealDto> observable = apiMealOperations.
                getMeals(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return observable;
    }

    public Observable<ListMealDto> getRandomMeal() {
        Observable<ListMealDto> observable = apiMealOperations.getRandomMeals().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<ListMealDto> getMealById(String id) {
        Observable<ListMealDto> observable = apiMealOperations.getMealById(id);
        return observable;
    }

    public Observable<ListCategoryDto> getAllCategories() {
        return apiMealOperations.getAllCategories("list").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListAreaDto> getAllAreas() {
        Observable<ListAreaDto> observable = apiMealOperations.getAllAreas("list").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<ListMealDto> filterByArea(String areaType) {
        return apiMealOperations.filterByArea(areaType)
                .subscribeOn(Schedulers.io()).
                doOnNext(listMealDto -> {
                    listMealDto.getMeals().forEach(mealDto -> {
                        getMealById(mealDto.getIdMeal()).doOnNext(mealDto1 -> {
                            Log.i(TAG, "filterByArea: "+mealDto.getIdMeal() );
                            Log.i(TAG, "filterByArea: " + mealDto.getStrArea()) ;}).subscribe();
                    });
                })
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListMealDto> filterByIngredients(String ingredientType) {
        return apiMealOperations.filterByIngredient(ingredientType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListMealDto> filterByCategory(String categoryType) {
        return apiMealOperations.filterByCategory(categoryType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListIngredientDto> getAllIngredients() {
        return apiMealOperations.getAllIngredients("list").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public void insertMealToFireBase(MealDto mealDto) {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        fireBasedatabase = FirebaseDatabase.getInstance();
        fireBasereference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("meals");

        fireBasereference.child(mealDto.getIdMeal()).setValue(mealDto)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Meal saved successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to save meal: " + e.getMessage());
                });
    }


    public void insertCalenderFireBase(MealsCalenderDto mealsCalenderDto) {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            String userId = auth.getCurrentUser().getUid();

            fireBasedatabase = FirebaseDatabase.getInstance();
            fireBasereference = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("Calender");

            fireBasereference.child(mealsCalenderDto.getDate() + " " + mealsCalenderDto.getMealId()).setValue(mealsCalenderDto)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Meal saved successfully!");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to save meal: " + e.getMessage());
                    });
        }
    }


    public DatabaseReference getMealsFromFireBase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid(); // Get the logged-in user

        DatabaseReference mealsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("meals");
        return mealsRef;


    }


    @Override
    public DatabaseReference getUserCalenderFireBase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid(); // Get the logged-in user

        DatabaseReference calendarRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("Calender");
        return calendarRef;

    }

}


