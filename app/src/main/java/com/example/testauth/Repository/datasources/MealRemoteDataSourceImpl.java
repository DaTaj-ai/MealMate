package com.example.testauth.Repository.datasources;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.example.testauth.Models.CategoryDto;
import com.example.testauth.Models.ListAreaDto;
import com.example.testauth.Models.ListCategoryDto;
import com.example.testauth.Models.ListIngredientDto;
import com.example.testauth.Models.ListMealDto;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.Models.UserDto;
import com.example.testauth.Network.ApiMealOperations;
import com.example.testauth.Network.NetworkCallBack;
import com.example.testauth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public Observable<ListCategoryDto> getAllCategories() {
        return apiMealOperations.getAllCategories("list").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListAreaDto> getAllAreas() {
        Observable<ListAreaDto> observable = apiMealOperations.getAllAreas("list").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<ListMealDto> filterByArea(String areaType) {
        return apiMealOperations.filterByArea(areaType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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

//        mealsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<MealDto> mealsList = new ArrayList<>();
//
//                for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
//                    MealDto meal = mealSnapshot.getValue(MealDto.class);
//                    if (meal != null) {
//                        mealsList.add(meal);
//                        Log.d(TAG, "Meal Found: " + meal.getIdMeal() );
//                    }
//                }
//
//                Log.d(TAG, "Total Calendar Entries Found: " + mealsList.size());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//            }
//        });
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
//        calendarRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<MealsCalenderDto> calendarList = new ArrayList<>();
//
//                for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
//                    MealsCalenderDto meal = mealSnapshot.getValue(MealsCalenderDto.class);
//                    if (meal != null) {
//                        calendarList.add(meal);
//                        Log.d(TAG, "Meal Found: " + meal.getMealId() + meal.getDate());
//                    }
//                }
//
//                Log.d(TAG, "Total Calendar Entries Found: " + calendarList.size());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//            }
//        });
    }

}


//        Query emailQuery = fireBasereference.orderByChild("email").equalTo(userUsername);
//        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//
//                    Log.i(TAG, "onDataChange: yes");
//                    // Email already exists;
//
//                } else {
//                    Log.i(TAG, "onDataChange:  + no ");
////                    UserDto userDto = new UserDto(nameStr, emailStr, passwordStr);
////                    fireBasereference.child(emailStr.replace(".", ",")).setValue(userDto)
////                            .addOnCompleteListener(new OnCompleteListener<Void>() {
////                                @Override
////                                public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
////                                    if (task.isSuccessful()) {
////                                        Snackbar snackbar = Snackbar.make(v, "Sign in successfully", Snackbar.LENGTH_LONG);
////                                        snackbar.show();
////                                        Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_signIn2);
////                                    } else {
////                                        Snackbar snackbar = Snackbar.make(v, task.getException().getMessage(), Snackbar.LENGTH_LONG);
////                                        snackbar.show();
////                                    }
////                                }
////                            });
////
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Snackbar snackbar = Snackbar.make(v, "Check your connection: ", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            }
//        });


