//package com.example.testauth.Network;
//
//import android.util.Log;
//
//
//import com.example.testauth.Models.ListMealDto;
//import com.example.testauth.Models.MealDto;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.annotations.NonNull;
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.core.Observer;
//import io.reactivex.rxjava3.disposables.Disposable;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class NetworkService {
//
//
//    public static NetworkService  instanse = null ;
//    List<MealDto> MealDtos = new ArrayList<>(Arrays.asList(
//            new MealDto()
//    ));
//
//    public static final String URL = "https://www.themealdb.com/api/json/v1/1/" ;
//
//    ApiMealOperations apiMealOperations;
//    private NetworkService(){
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
//        apiMealOperations = retrofit.create(ApiMealOperations.class);
//    }
//
//    public static NetworkService getInstance(){
//        if(instanse == null){
//            instanse = new NetworkService();
//        }
//        return instanse ;
//    }
//
//    private static final String TAG = "NetworkService";
//    public List<MealDto> getMeals(NetworkCallBack networkCallBack) {
//
//        Observable<ListMealDto> observable  =  apiMealOperations.getMeals("a").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());;
//
//        List<MealDto> MealDtosLocal  = new ArrayList<>();
//
//        Observer<ListMealDto> observer = new Observer<ListMealDto>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.i(TAG, "onSubscribe: ");
//            }
//
//            @Override
//            public void onNext(@NonNull ListMealDto listMealDto) {
//
//                networkCallBack.onSuccess(listMealDto.getMeals());
//                Log.i(TAG, "onNext: " + listMealDto.getMeals() );
//
//                for(MealDto mealDto : listMealDto.getMeals()){
//                    Log.i(TAG, "onNext: " + mealDto.getIdMeal());
//                }
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                Log.i(TAG, "onError: " + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        };
//
//        observable.subscribe(observer);
//
//        return MealDtosLocal ;
//    }
//}
